import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../shared/models/paginationArgs.model';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { FormService } from '../../shared/services/form.service';
import { Form } from 'src/app/shared/models/form.model';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { FormResponseModel } from '../../shared/models/form-response-model';
import { DateTimeFormatPipe } from '../../shared/pipes/date-time-format.pipe';
import { FilterValuesModel } from '../../shared/models/filter-values-model';

@Component({
  selector: 'app-form-management',
  templateUrl: './form-management.component.html',
  styleUrl: './form-management.component.scss',
  providers: [DateTimeFormatPipe]
})
export class FormManagementComponent implements OnInit {
  private readonly paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected form: Form[] = [];
  protected pageSize: number;
  protected page: number = 0;
  public totalCount: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected formId: string;
  protected formName: string;
  protected totalElements: number;
  private paginationArgs: PaginationArgs;
  private filterValues: FilterValuesModel[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private formService: FormService,
    private cdRef: ChangeDetectorRef,
    private toasterService: AppToastNotificationService,
    private dateTimeFormatPipe: DateTimeFormatPipe
  ) {}

  public ngOnInit(): void {
    this.pageSize = 5;
    this.loadForm();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.page = event.pageNumber - 1;
    this.loadForm();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadForm();
  }

  private loadForm(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.formService.getForms(this.filterValues, this.paginationArgs).subscribe((response: FormResponseModel) => {
      const totalCountHeader = response?.headers.get('X-Total-Count');
      this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
      this.form = response?.body?.map((item, index) => ({
        ...item,
        createdDate: this.dateTimeFormatPipe.transform(item.createdDate),
        index: this.page * this.pageSize + index + 1
      }));
    });
  }

  public redirectToAddForm(): void {
    this.router.navigate(['/pages/form-management/form-builder/add']);
  }

  public ImportForm(): void {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';

    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        if (file.type !== 'application/json' && file.name.split('.').pop() !== 'json') {
          this.toasterService.onError(this.translatePipe.transform('form.IMPORT_FAILED'), this.translatePipe.transform('menu.ERROR'));
          return;
        }
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const fileContent = e.target.result;
          const parsedFields = JSON.parse(fileContent);
          const formDTO = {
            fields: parsedFields,
            label: '',
            description: ''
          };

          const navigationExtras: NavigationExtras = {
            relativeTo: this.route,
            state: {
              formDTO: formDTO
            }
          };
          this.router.navigate(['/pages/form-management/form-builder/add'], navigationExtras);
        };
        reader.readAsText(file);
      }
    };

    input.click();
  }
  handleOnViewForm(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        formId: id
      }
    };
    this.router.navigate(['/pages/form-management/view-form', label], navigationExtras);
  }

  handleOnUpdateForm(id: string, formDTO: Form, label) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        formId: id,
        formDTO: formDTO
      }
    };
    this.router.navigate(['/pages/form-management/update-form', label], navigationExtras);
  }

  private updateTable(): void {
    this.form = [];
    this.loadForm();
    this.cdRef.detectChanges();
  }

  private handleOnDuplicateForm(formDTO: Form): void {
    this.formService.duplicateForm(formDTO.id).subscribe({
      next: () => {
        this.toasterService.onSuccess(this.translatePipe.transform('form.DUPLICATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
      },
      error: (error: any) => {
        this.toasterService.onError(this.translatePipe.transform('form.DUPLICATE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected handleOnDeleteForm(): void {
    this.formService.deleteForm(this.formId).subscribe({
      next: () => {
        this.updateTable();
        this.deleteUserModal.hide();
      },
      error: (error) => {
        console.error('Form delete failed', error);
      }
    });
  }

  handleOnExportForm(id: string, formDTO: Form): void {
    this.formService.getFormById(id).subscribe(
      (data) => {
        formDTO = data;
        const templateForm = JSON.parse(formDTO.fields);
        const jsonData = {
          ...templateForm
        };
        const blob = new Blob([JSON.stringify(jsonData, null, 2)], { type: 'application/json' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `${formDTO.label}.json`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        this.toasterService.onSuccess(this.translatePipe.transform('form.EXPORT_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
      },
      (error) => {
        console.error('Error retrieving form:', error);
        this.toasterService.onError(this.translatePipe.transform('form.EXPORT_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    );
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewForm(event.objectClicked.id, event.objectClicked.label);
    }
    if (event.buttonActionId === 'edit') {
      if (event.objectClicked.isDeleted === true) {
        this.toasterService.onError(this.translatePipe.transform('form.CANT_DO_ACTION'), this.translatePipe.transform('menu.ERROR'));
      } else {
        this.handleOnUpdateForm(event.objectClicked.id, event.objectClicked, event.objectClicked.label);
      }
    }
    if (event.buttonActionId === 'duplicate') {
      if (event.objectClicked.isDeleted === true) {
        this.toasterService.onError(this.translatePipe.transform('form.CANT_DO_ACTION'), this.translatePipe.transform('menu.ERROR'));
      } else {
        this.handleOnDuplicateForm(event.objectClicked);
      }
    }
    if (event.buttonActionId === 'delete') {
      if (event.objectClicked.isDeleted === true) {
        this.toasterService.onError(this.translatePipe.transform('form.CANT_DO_ACTION'), this.translatePipe.transform('menu.ERROR'));
      } else {
        this.deleteUserModal.show();
        this.formId = event.objectClicked.id;
        this.formName = event.objectClicked.label;
      }
    }
    if (event.buttonActionId === 'export') {
      this.handleOnExportForm(event.objectClicked.id, event.objectClicked);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('duplicate', 'button.DUPLICATE', 'btn btn-icon', 'feather icon-copy'));
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
    this.actionButton.push(new ButtonActionModel('export', 'button.EXPORT', 'btn btn-icon', 'feather icon-download'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('form.ID', 'index', false));
    this.tableColumns.push(new CustomTableColonneModel('form.NOM', 'label', false));
    this.tableColumns.push(new CustomTableColonneModel('form.DESCRIPTION', 'description', false));
    this.tableColumns.push(new CustomTableColonneModel('form.VERSION', 'version', false));
    this.tableColumns.push(new CustomTableColonneModel('form.DATE_CREATION', 'createdDate', false));
  }
}
