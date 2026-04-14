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
import {DesignationsList} from '../../shared/models/designation';
import {UiModalComponent} from '../../theme/shared/components/modal/ui-modal/ui-modal.component';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import {MembreDesigneService} from '../../shared/services/membre-designe.service';
import {UserService} from '../../shared/services/user.service';
import {AppToastNotificationService} from '../../shared/services/appToastNotification.service';
import {DateformatPipe} from '../../shared/pipes/date-transform.pipe';
import {TokenUtilsService} from '../../shared/services/token-utils.service';
import {Authorities} from '../../shared/enums/authorities.enum';
import {DesignationResponseModel} from '../../shared/models/designation-response-model';
import {ApplicationsRequest} from "../../shared/models/ApplicationsRequest-model";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-membre-assignee',
  templateUrl: './membre-assignee.component.html',
  styleUrl: './membre-assignee.component.scss'
})
export class MembreAssigneeComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs(
      'lastModifiedDate',
      PaginationSortOrderType.DESC
  );
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

  public role_ectd: { id: string; label: string }[] = [];
  public applications_id: { label: string; index: number }[] = [];

  private pmUserId: string;
  protected isAuthorizedToAdd: boolean = false;
  public hasRole: boolean = false;

  public designationForm!: FormGroup;

  constructor(
      private router: Router,
      private route: ActivatedRoute,
      private translatePipe: TranslatePipe,
      private membreDesigneService: MembreDesigneService,
      private userService: UserService,
      private cdRef: ChangeDetectorRef,
      private toasterService: AppToastNotificationService,
      private translateService: TranslateService,
      private dateTransformPipe: DateformatPipe,
      private jwtUtils: TokenUtilsService,
      private tokenUtilsService: TokenUtilsService,
      private fb: FormBuilder,
      private toastrService: AppToastNotificationService,
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
      next: () => {
        this.loadUsers();
      }
    });
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
      );
    }
  }

  private initTableColumns(): void {
    this.tableColumns.push(
        new CustomTableColonneModel('designation.FIRSTNAME', 'representativeName', false)
    );
    this.tableColumns.push(
        new CustomTableColonneModel('designation.CREATE_DDATE', 'createdDate', false)
    );
    this.tableColumns.push(
        new CustomTableColonneModel('designation.ROLE', 'roleDescription', false)
    );
  }

  protected loadUsers(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };

    this.pmUserId = this.jwtUtils.getUserId();

    this.membreDesigneService
    .getListByDesignatedUserId(this.filterValues, this.pmUserId, this.paginationArgs)
    .subscribe({
      next: (response: DesignationResponseModel) => {
        const totalCountHeader = response?.headers.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.designations = response?.body?.map((item, index) => ({
          ...item,
          index: this.pageIndex * this.pageSize + index + 1,
          representativeName:
              item.pmUser
                  ? item.pmUser.firstName && item.pmUser.lastName
                      ? `${item.pmUser.firstName} ${item.pmUser.lastName}`
                      : item.pmUser?.denomination || ''
                  : '',
          createdDate: this.dateTransformPipe.transformDateAndHours(item.createdDate),
          roleDescription: item.pmUser.roles?.find(role => role.description !== 'redirection ECTD')?.description || ''
        }));
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
  private hasEctdRole(row: any): boolean {
    const roles = row?.designatedUser.roles ?? [];

    const hasEctd = roles.some(r => r.label === 'BS_ROLE_ECTD');
    const hasLabo =
        roles.some(r => r.label === 'BS_ROLE_PM_LABO_ETRAN') ||
        roles.some(r => r.label === 'BS_ROLE_PM_LABO_LOC');

    return hasEctd && !hasLabo;
  }
}
