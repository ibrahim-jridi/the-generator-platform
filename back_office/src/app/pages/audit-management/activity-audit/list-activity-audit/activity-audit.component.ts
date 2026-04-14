import { Component, OnInit } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../../shared/models/paginationArgs.model';
import { CustomTableColonneModel } from '../../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { AppToastNotificationService } from '../../../../shared/services/appToastNotification.service';
import { ActivityAuditModel } from '../../../../shared/models/activity-audit.model';
import { ActivityAuditService } from '../../../../shared/services/activity-audit.service';
import { formatDate } from '@angular/common';
import { ButtonActionModel } from '../../../../theme/shared/components/custom-table/model/button-action.model';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { FilterValuesModel } from '../../../../shared/models/filter-values-model';
import { ActivityAuditResponseModel } from '../../../../shared/models/activity-audit-response.model';

@Component({
  selector: 'app-activity-audit',
  templateUrl: './activity-audit.component.html',
  styleUrls: ['./activity-audit.component.scss']
})
export class ActivityAuditComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('submissionDate', PaginationSortOrderType.DESC);
  public paginationArgs: PaginationArgs;
  public activityAuditList: ActivityAuditModel[] = [];
  public tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  public actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  filterValues: FilterValuesModel[];

  constructor(
    private activityAuditService: ActivityAuditService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: AppToastNotificationService
  ) {}

  public ngOnInit(): void {
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    this.loadAuditData();
  }

  private loadAuditData(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.pageIndex, size: this.pageSize };
    this.activityAuditService.getActivityAudits(this.filterValues, this.paginationArgs).subscribe({
      next: (response: ActivityAuditResponseModel) => {
        const totalCountHeader = response.headers?.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.activityAuditList = this.formatActivityAudit(response.body);
      },
      error: () => {
        this.toastService.onError(this.translatePipe.transform('audit.errors.FAILED_TO_LOAD_AUDIT'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public formattedDate(date: Date): string {
    if (date == null) {
      return 'N/A';
    }
    return formatDate(date, 'dd/MM/yyyy HH:mm:ss', 'en-US');
  }

  protected formatActivityAudit(auditList: any[]): any[] {
    return auditList
      .filter((audit) => audit.submissionDate)
      .map((audit, index) => {
        const formattedAudit = { ...audit };
        formattedAudit.index = this.pageSize * this.pageIndex + index + 1;
        formattedAudit.submissionDate = this.formattedDate(audit.submissionDate);
        formattedAudit.entity = this.translatePipe.transform(`entity.${audit.entity}`);
        formattedAudit.action = this.translatePipe.transform(`audit.${audit.action}`);
        return formattedAudit;
      });
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.NUMBER'), 'index', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.DATE'), 'submissionDate', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.ENTITY'), 'entity', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.USERS'), 'userFullName', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('audit.ACTION'), 'action', false));
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadAuditData();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.navigateToView(event.objectClicked.userFullName, event.objectClicked.id);
    }
  }

  public navigateToView(user: string, id: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        auditId: id,
        user: user
      }
    };
    this.router.navigate(['view-activity-audit', user], navigationExtras);
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', this.translatePipe.transform('button.VIEW'), 'btn btn-icon ', 'feather icon-eye'));
  }

  public nextPage(event: any) {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.loadAuditData();
  }
}
