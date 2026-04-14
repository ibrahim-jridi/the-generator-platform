import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ReportService } from '../../../shared/services/report.service';

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrl: './report-list.component.scss'
})
export class ReportListComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected rapport: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected reportId: string;
  protected totalElements: number;
  protected currentUserStationId: string;
  private paginationArgs: PaginationArgs;

  constructor(private router: Router, private route: ActivatedRoute, private translatePipe: TranslatePipe, private reportService: ReportService) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadReport();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadReport();
  }

  private loadReport(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.reportService.getTemplates().subscribe((data) => {
      this.rapport = data.map((template, index) => ({
        index: this.page * this.pageSize + index + 1,
        id: template.id,
        name: template.name,
        description: template.description
      }));
    });
  }

  public redirectToAddReport(): void {
    this.router.navigate(['/pages/report-management/add']);
  }

  handleOnViewReport(reportId: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        reportId: reportId
      }
    };
    this.router.navigate(['view-report', label], navigationExtras);
  }

  handleOnUpdateReport(reportId: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        reportId: reportId
      }
    };
    this.router.navigate(['update-report', label], navigationExtras);
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewReport(event.objectClicked.id, event.objectClicked.name);
      //this.router.navigate(['pages/user-management/internal-user-management/view'])
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateReport(event.objectClicked.id, event.objectClicked.name);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('report.INDEX', 'index', false));
    this.tableColumns.push(new CustomTableColonneModel('report.NOM', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('report.DESCRIPTION', 'description', false));
  }
}
