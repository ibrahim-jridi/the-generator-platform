import { Component, OnInit } from '@angular/core';
import { CustomTableColonneModel } from '../../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ButtonActionModel } from '../../../../theme/shared/components/custom-table/model/button-action.model';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../../shared/models/paginationArgs.model';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from '../../../../shared/services/appToastNotification.service';
import { formatDate } from '@angular/common';
import { ErrorAuditModel } from '../../../../shared/models/error-audit.model';
import { ErrorAuditService } from '../../../../shared/services/error-audit.service';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';

import { FilterValuesModel } from '../../../../shared/models/filter-values-model';
import { ErrorAuditResponseModel } from '../../../../shared/models/error-audit-response.model';

@Component({
  selector: 'app-error-audit',
  templateUrl: './error-audit.component.html',
  styleUrl: './error-audit.component.scss'
})
export class ErrorAuditComponent implements OnInit {
  public errorAuditList: ErrorAuditModel[] = [];
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  public tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  public actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('createdDate', PaginationSortOrderType.DESC);
  public paginationArgs: PaginationArgs;
  filterValues: FilterValuesModel[];

  constructor(
    private errorAuditService: ErrorAuditService,
    private translatePipe: TranslatePipe,
    private route: ActivatedRoute,
    private router: Router,
    private toastrService: AppToastNotificationService
  ) {}

  public ngOnInit(): void {
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    this.getAllErrorAudit();
  }

  public getAllErrorAudit(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.pageIndex, size: this.pageSize };
    this.errorAuditService.getAllErrorAudits(this.filterValues, this.paginationArgs).subscribe({
      next: (result: ErrorAuditResponseModel) => {
        const totalCountHeader = result?.headers?.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.errorAuditList = this.formatErrorAudit(result?.body);
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('audit.errors.FAILED_TO_LOAD_ERROR_AUDIT'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }

  protected formatErrorAudit(auditList: any[]): any[] {
    return auditList.map((audit, index) => {
      const formattedAudit = { ...audit };
      formattedAudit.createdDate = this.formatDate(audit.createdDate);
      formattedAudit.errorType = this.translatePipe.transform(`audit.error_type.${audit.errorType}`);
      return formattedAudit;
    });
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.getAllErrorAudit();
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.getAllErrorAudit();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.navigateToView(event.objectClicked.createdDate, event.objectClicked.id);
    }
  }

  public navigateToView(date: any, id: number) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        auditId: id,
        date: date
      }
    };

    this.router.navigate(['view-error-audit', date], navigationExtras);
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.TYPE'), 'errorType', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.DESCRIPTION'), 'description', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.CREATED_DATE'), 'createdDate', false));
  }

  private formatDate(date: any): string {
    return formatDate(date, 'dd/MM/yyyy HH:mm:ss', 'en-US');
  }
}
