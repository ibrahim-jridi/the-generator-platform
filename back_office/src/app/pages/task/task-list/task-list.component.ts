import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from 'src/app/shared/models/paginationArgs.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { TokenUtilsService } from '../../../shared/services/token-utils.service';
import { CamundaService } from '../../../shared/services/camunda.service';
import { DateformatPipe } from '../../../shared/pipes/date-transform.pipe';
import { formatDate } from '@angular/common';
import { TaskCriteria } from '../../../shared/models/task-criteria';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss',
  providers: [DateformatPipe]
})
export class TaskListComponent implements OnInit {
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
    private toasterService: AppToastNotificationService,
    private tokenUtils: TokenUtilsService,
    private readonly route: ActivatedRoute,
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
    this.camundaService.getActiveTasksByUserId(this.paginationArgs).subscribe({
      next: (response) => {
        this.totalCount = response.totalElements;
        this.camundaService.taskValidateSubject.next(this.totalCount);
        this.tasks = response.content.map((item, index) => ({
          ...item,
          createTime: this.dateTransformPipe.transformDateAndHours(item.createTime)
        }));
      },
      error: (error) => {
        this.toasterService.onError(this.translatePipe.transform('groups.failedToLoadTasks'), this.translatePipe.transform('menu.failedToLoadTasks'));
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
    this.actionButton.push(new ButtonActionModel('validate', 'button.VALIDATE', 'btn btn-icon', 'feather icon-check-square'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('task.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('task.DATE_CREATION', 'createTime', false));
    this.tableColumns.push(new CustomTableColonneModel('task.NOM_TACHE', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('task.NOM_DEMANDEUR', 'owner', false));
    this.tableColumns.push(new CustomTableColonneModel('task.INSTANCE_KEY', 'businessKey', false));
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.criteria = new TaskCriteria(
      this.filterValues.find((f) => f.columnName === 'id')?.filterValue,
      new Date(this.filterValues.find((f) => f.columnName === 'createTime')?.filterValue),
      this.filterValues.find((f) => f.columnName === 'name')?.filterValue,
      null,
      this.filterValues.find((f) => f.columnName === 'businessKey')?.filterValue
    ),
      this.filterValues.find((f) => f.columnName === 'owner')?.filterValue;
    this.page = event.pageNumber - 1;
    this.loadTasks();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadTasks();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'validate') {
      this.handleOnValidateTask(event.objectClicked.id, event.objectClicked.processInstanceId, event.objectClicked.name);
    }
  }

  private handleOnValidateTask(taskId: string, processInstanceId: string, label: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        processInstanceId: processInstanceId,
        taskId: taskId,
        taskName: label
      }
    };
    this.router.navigate(['validate-task', taskId], navigationExtras);
  }
}
