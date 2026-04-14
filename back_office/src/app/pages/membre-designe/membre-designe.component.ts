import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  PaginationArgs,
  PaginationSortArgs,
  PaginationSortOrderType
} from '../../shared/models/paginationArgs.model';
import {
  CustomTableColonneModel
} from '../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import {
  ButtonActionModel
} from '../../theme/shared/components/custom-table/model/button-action.model';
import {FilterValuesModel} from '../../shared/models/filter-values-model';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import {AppToastNotificationService} from '../../shared/services/appToastNotification.service';
import {MembreDesigneService} from '../../shared/services/membre-designe.service';
import {DesignationResponseModel} from '../../shared/models/designation-response-model';
import {DateformatPipe} from '../../shared/pipes/date-transform.pipe';
import {DesignationsList} from '../../shared/models/designation';
import {UiModalComponent} from '../../theme/shared/components/modal/ui-modal/ui-modal.component';
import {UserService} from '../../shared/services/user.service';
import {TokenUtilsService} from '../../shared/services/token-utils.service';
import {Authorities} from '../../shared/enums/authorities.enum';
import {ApplicationsRequest} from "../../shared/models/ApplicationsRequest-model";
import {forkJoin} from "rxjs";
import { CamundaService } from '../../shared/services/camunda.service';

@Component({
  selector: 'app-membre-designe',
  templateUrl: './membre-designe.component.html',
  styleUrl: './membre-designe.component.scss'
})
export class MembreDesigneComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  protected tableColumns: CustomTableColonneModel[] = [];
  protected actionButton: ButtonActionModel[] = [];
  public paginationArgs: PaginationArgs;
  public totalCount: number;
  public pageIndex: number = 0;
  protected pageSize: number;
  filterValues: FilterValuesModel[] = [];
  protected designations: DesignationsList[] = [];
  protected selectedItemsArray: any[] = [];

  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  @ViewChild('deleteDesignationsModal') protected deleteDesignationsModal: UiModalComponent;

  public role_ectd: { id: string; label: string }[] = [];
  public applications_id: { label: string; index: number }[] = [];

  private pmUserId: string;
  protected isAuthorizedToAdd: boolean = false;
  public hasRole: boolean = false;

  public designationForm!: FormGroup;
  private processDefKey: string = 'ADD_DESIGNATION_MODEL_LTS';
  protected userId: string;
  private itemToDelete: any;

  constructor(
      private router: Router,
      private translatePipe: TranslatePipe,
      private membreDesigneService: MembreDesigneService,
      private toasterService: AppToastNotificationService,
      private translateService: TranslateService,
      private dateTransformPipe: DateformatPipe,
      private jwtUtils: TokenUtilsService,
      private tokenUtilsService: TokenUtilsService,
      private fb: FormBuilder,
      private toastrService: AppToastNotificationService,
      private camundaService: CamundaService,
      private tokenUtilisService: TokenUtilsService,
  ) {
  }

  ngOnInit(): void {
    this.pageSize = 5;
    this.isAuthorizedToAdd = this.tokenUtilsService
    .getAuthorities()
    .includes(Authorities.BS_ECTD);

    this.initTableAction();
    this.initTableColumns();
    this.loadUsers();
    this.loadDesignationRole();
    this.loadRequest();
    this.initForm();

    this.translateService.onLangChange.subscribe({
      next: () => this.loadUsers()
    });
    this.userId = this.tokenUtilisService.getUserId();
  }

  private initForm(): void {
    this.designationForm = this.fb.group({
      role: [null, Validators.required],
      requests: [[]]
    });
  }

  public nextPage(event: any): void {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.loadUsers();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadUsers();
  }

  private initTableAction(): void {
    if (this.isAuthorizedToAdd) {
      this.actionButton.push(
          new ButtonActionModel('add', 'button.ADD', 'btn btn-icon', 'feather icon-plus-square', (row) => !this.hasEctdRole(row)),
          new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash')
      );
    }
    else {
      this.actionButton.push(
          new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash')

      );
    }
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('designation.FIRSTNAME', 'representativeName', false));
    this.tableColumns.push(new CustomTableColonneModel('designation.CREATE_DDATE', 'createdDate', false));
    this.tableColumns.push(new CustomTableColonneModel('designation.ROLE', 'roleDescription', false));
    this.tableColumns.push(new CustomTableColonneModel('designation.LABORATORY', 'laboratoryUserName', false));
  }

  protected loadUsers(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };

    this.pmUserId = this.jwtUtils.getUserId();
    this.membreDesigneService
    .getListByPmUserId(this.filterValues, this.pmUserId, this.paginationArgs)
    .subscribe({
      next: (response: DesignationResponseModel) => {
        const totalCountHeader = response?.headers.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.designations = response?.body?.map((item, index) => ({
          ...item,
          index: this.pageIndex * this.pageSize + index + 1,
          representativeName: item.designatedUser
              ? item.designatedUser.firstName && item.designatedUser.lastName
                  ? `${item.designatedUser.firstName} ${item.designatedUser.lastName}`
                  : item.designatedUser.denomination || ''
              : '',
          createdDate: this.dateTransformPipe.transformDateAndHours(item.createdDate),
          roleDescription: item.role?.description,
          laboratoryUserName: item.laboratoryUser
              ? item.laboratoryUser.firstName && item.laboratoryUser.lastName
                  ? `${item.laboratoryUser.firstName} ${item.laboratoryUser.lastName}`
                  : item.laboratoryUser.denomination || item.laboratoryUser.username || ''
              : ''        }));
      }
    });
  }

  protected buttonAction(event: any): void {
    const clickedItem = event.objectClicked;

    if (event.buttonActionId === 'add') {
      this.deleteUserModal.show();

      if (clickedItem && clickedItem.id) {
        const selectedItem = this.designations.find(item => item.id === clickedItem.id);
        this.selectedItemsArray = [
          {keycloakId: selectedItem?.designatedUser?.keycloakId || ''}
        ];
      }

      if (clickedItem && clickedItem.designatedUser && clickedItem.designatedUser.keycloakId) {

        forkJoin(
            this.membreDesigneService.getEctdRoleByKeycloakId(clickedItem.designatedUser.keycloakId),
            this.membreDesigneService.getApplicationIdsByKeycloakId(clickedItem.designatedUser.keycloakId)
        ).subscribe(resp => {
          if (resp[0] && resp[0].status === 200 && resp[0].body) {
            let roles: string[] = resp[0].body;
            if (roles.length > 0) {
              this.designationForm.patchValue({
                role: roles[0]
              });
            }
          }

          if (resp[1] && resp[1].status === 200 && resp[1].body) {
            let applicationIds: string[] = resp[1].body;

            if (applicationIds.length > 0) {
              this.designationForm.patchValue({
                requests: applicationIds
              });
            }

          }
        })

      }

    }
    if (event.buttonActionId === 'delete') {
      this.itemToDelete = clickedItem;
      this.deleteDesignationsModal.show();
    }
  }

  private loadDesignationRole(): void {
    this.role_ectd = [
      { id: 'aucun', label: 'Aucun' },
      { id: 'editor', label: 'Editor' },
      { id: 'submitter', label: 'Submitter' },
      { id: 'viewer', label: 'Viewer' }
    ];
  }

  protected loadRequest(): void {
    this.membreDesigneService.getApplicationNames().subscribe({
      next: (response: ApplicationsRequest) => {
        if (response && response.names) {
          this.applications_id = response.names.map((item, index) => ({
            label: item,
            index: this.pageIndex * this.pageSize + index + 1
          }));
        }
      }
    });
  }

  protected saveDesignationUser(): void {
    if (this.designationForm.invalid) {
      this.designationForm.markAllAsTouched();
      return;
    }

    const {role, requests} = this.designationForm.value;

    const designationData = {
      keycloakId:
          this.selectedItemsArray.length > 0
              ? this.selectedItemsArray[0].keycloakId
              : '',
      ectdRole: role ? role.id : '',
      applicationsId: requests
    };

    this.membreDesigneService.updateKeycloakUserDesignations(designationData).subscribe({
      next: (response: string) => {
        if (response === 'User attributes updated successfully in Keycloak') {
          this.toasterService.onSuccess(
              this.translatePipe.transform('designation.ADD_SUCCESS'),
              this.translatePipe.transform('menu.SUCCESS')
          );
          this.designationForm.reset();
          this.deleteUserModal.hide();
        }
      },
      error: () => {
        this.toasterService.onError(
            this.translatePipe.transform('designation.ADD_ERROR'),
            this.translatePipe.transform('menu.ERROR')
        );
      }
    });

    this.designationForm.reset({
      role: null,
      requests: []
    });

  }
  startProcessByKeyAndRedirectToValidateTask() {
    const vars = {
      starter: this.userId,
    };
    this.camundaService.startUniqueProcessInstanceByKey(this.processDefKey, vars).subscribe(
      (res) => {
        this.camundaService.getTaskByProcessInstanceId(res.processInstanceId).subscribe(
          (data) => {
            const navigationExtras: NavigationExtras = {
              state: {
                processInstanceId: data.executionId,
                taskId: data.id
              }
            };
            this.router.navigate(['pages/task-management/task-list/validate-task', data.name], navigationExtras);
          },
          (error) => {
            this.toastrService.onError(this.translatePipe.transform('task.FAILED_TO_GET_TASK'), this.translatePipe.transform('menu.ERROR'));
          }
        );
            },
      (err) => {
        if (err?.error?.detail?.includes('An active instance of the process already exists for user')) {
          this.toastrService.onInfo(
            this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'),
            this.translatePipe.transform('menu.INFO')
          );
        } else {
          this.toastrService.onInfo(
              this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'),
              this.translatePipe.transform('menu.INFO')
          );
        }
      }
    );
  }

  checkAuthorities(authority): boolean {
    return this.tokenUtilisService.hasUserRole(authority);
  }
  private hasEctdRole(row: any): boolean {
    const roles = row?.designatedUser.roles ?? [];

    const hasEctd = roles.some(r => r.label === 'BS_ROLE_ECTD');
    const hasLabo =
        roles.some(r => r.label === 'BS_ROLE_PM_LABO_ETRAN') ||
        roles.some(r => r.label === 'BS_ROLE_PM_LABO_LOC');

    return hasEctd && !hasLabo;
  }
  confirmDelete(): void {
    this.membreDesigneService.deleteDesignation(this.itemToDelete.id).subscribe(
        (data) => {
          this.toastrService.onSuccess(
              this.translatePipe.transform('users.DELETE_SUCCESS'),
              this.translatePipe.transform('menu.SUCCESS')
          );

          this.loadUsers();
          this.loadDesignationRole();
          this.loadRequest();
          this.deleteDesignationsModal.hide();
          this.itemToDelete = null;
        },
        (error) => {
          this.toastrService.onError(
              this.translatePipe.transform('users.DELETE_FAILED'),
              this.translatePipe.transform('menu.ERROR')
          );

          this.deleteDesignationsModal.hide();

          this.itemToDelete = null;
        }
    );
  }
  protected back(): void {
    this.deleteDesignationsModal.hide();

    this.itemToDelete = null;
  }
}
