import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ReportService } from 'src/app/shared/services/report.service';

@Component({
  selector: 'app-report-view',
  templateUrl: './report-view.component.html',
  styleUrl: './report-view.component.scss'
})
export class ReportViewComponent implements OnInit {
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
  private templateId: string;
  public state = window.history.state;

  constructor(private router: Router, private route: ActivatedRoute, private translatePipe: TranslatePipe, private reportService: ReportService) {}

  public ngOnInit(): void {
    this.reportId = this.state.reportId;
    this.page = 0;
    this.pageSize = 5;
    this.loadRapport();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadRapport();
  }

  private loadRapport(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.reportService.getReportsByTemplateId(this.reportId).subscribe((data) => {
      this.rapport = data.map((report, index) => ({
        index: this.page * this.pageSize + index + 1,
        id: report.id,
        name: report.name,
        createdDate: report.createdDate
      }));
    });
  }

  public redirectToAddReport(): void {
    this.router.navigate(['/pages/report-management/add']);
  }

  handleOnViewReport(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        reportId: id,
        userName: label // Database id
      }
    };
    //this.router.navigate(['view-report', label], navigationExtras);
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewReport(event.objectClicked.id, event.objectClicked.nom);
      //this.router.navigate(['pages/user-management/internal-user-management/view'])
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', this.translatePipe.transform('button.VIEW'), 'btn btn-icon ', 'feather icon-eye'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('report.INDEX'), 'index', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('report.NOM'), 'name', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('report.DATE_CREATION'), 'createdDate', false));
  }
}
