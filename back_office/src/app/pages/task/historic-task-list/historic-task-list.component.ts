import { Component, OnInit } from '@angular/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { CamundaService } from '../../../shared/services/camunda.service';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { TokenUtilsService } from '../../../shared/services/token-utils.service';
import { formatDate } from '@angular/common';
import { TaskCriteria } from '../../../shared/models/task-criteria';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { ProcessDefinition } from '../../../shared/models/processDefinition.model';
import { DateformatPipe } from '../../../shared/pipes/date-transform.pipe';

@Component({
  selector: 'app-historic-task-list',
  templateUrl: './historic-task-list.component.html',
  styleUrl: './historic-task-list.component.scss'
})
export class HistoricTaskListComponent implements OnInit {
  private paginationArgs: PaginationArgs;
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('startTime', PaginationSortOrderType.DESC);
  protected tasks: any[] = [];
  public totalCount: number;
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected userId: string;
  protected taskId: string;
  private criteria: TaskCriteria = new TaskCriteria();
  private filterValues: FilterValuesModel[];

  constructor(
    private router: Router,
    private camundaService: CamundaService,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private tokenUtils: TokenUtilsService,
    private route: ActivatedRoute,
    private dateTransformPipe: DateformatPipe
  ) {}

  public ngOnInit(): void {
    this.userId = this.tokenUtils.getUserId();
    this.page = 0;
    this.pageSize = 5;
    this.loadTasks();
    this.initTableAction();
    this.initTableColumns();
  }

  private loadTasks(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.camundaService.getHistoricTasksByAssigneeAndCriteria(this.userId, this.criteria, this.paginationArgs).subscribe({
      next: (response) => {
        this.totalCount = response.totalElements;
        this.tasks = response.content.map((item, index) => ({
          ...item,
          startTime: this.dateTransformPipe.transformDateAndHours(item.startTime),
          endTime: this.dateTransformPipe.transformDateAndHours(item.endTime)
        }));
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('groups.failedToLoadTasks'), this.translatePipe.transform('menu.failedToLoadTasks'));
      }
    });
  }

  public formattedDate(date: Date): string {
    if (date == null) {
      return 'N/A';
    }
    return formatDate(date, 'dd/MM/yyyy HH:mm:ss', 'en-US');
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('task.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('task.DATE_CREATION', 'startTime', false));
    this.tableColumns.push(new CustomTableColonneModel('task.NOM_TACHE', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('task.NOM_DEMANDEUR', 'owner', false));
    this.tableColumns.push(new CustomTableColonneModel('task.DATE_VALIDATION', 'endTime', false));
    this.tableColumns.push(new CustomTableColonneModel('task.INSTANCE_KEY', 'businessKey', false));
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.criteria = new TaskCriteria(
      this.filterValues.find((f) => f.columnName === 'id')?.filterValue,
      new Date(this.filterValues.find((f) => f.columnName === 'startTime')?.filterValue),
      this.filterValues.find((f) => f.columnName === 'name')?.filterValue,
      new Date(this.filterValues.find((f) => f.columnName === 'endTime')?.filterValue),
      this.filterValues.find((f) => f.columnName === 'businessKey')?.filterValue,
      this.filterValues.find((f) => f.columnName === 'owner')?.filterValue
    );
    this.page = event.pageNumber - 1;
    this.loadTasks();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadTasks();
  }

  public buttonAction(event: any): void {
    this.camundaService.getProcessDefinitionById(event.objectClicked.processDefinitionId).subscribe({
      next: (response: ProcessDefinition) => {
        if (event.buttonActionId === 'view') {
          this.handleOnViewTask(event.objectClicked.processInstanceId, response.name);
        }
      }
    });
  }

  private handleOnViewTask(processInstanceId: string, name: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        processInstanceId: processInstanceId
      }
    };
    this.router.navigate(['view-historic-task', name], navigationExtras);
  }
}
