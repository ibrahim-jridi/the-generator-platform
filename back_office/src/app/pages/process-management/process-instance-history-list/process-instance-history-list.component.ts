import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { FilterValuesModel } from 'src/app/shared/models/filter-values-model';
import { HistoricTaskInstance } from 'src/app/shared/models/historicTaskInstance.model';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from 'src/app/shared/models/paginationArgs.model';
import { ProcessInstanceHistory } from 'src/app/shared/models/process-instance-history';
import { ProcessInstanceHistoryCriteria } from 'src/app/shared/models/process-instance-history-criteria';
import { ProcessDefinition } from 'src/app/shared/models/processDefinition.model';
import { DateformatPipe } from 'src/app/shared/pipes/date-transform.pipe';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { CamundaService } from 'src/app/shared/services/camunda.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';

@Component({
  selector: 'app-process-instance-history-list',
  templateUrl: './process-instance-history-list.component.html',
  styleUrl: './process-instance-history-list.component.scss'
})
export class ProcessInstanceHistoryListComponent implements OnInit{

  private paginationArgs: PaginationArgs;
    private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('startTime', PaginationSortOrderType.DESC);
    protected processInstanceHistory: ProcessInstanceHistory[] = [];
    public totalCount: number;
    protected pageSize: number;
    protected page: number;
    protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
    protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
    protected userId: string;
    protected taskId: string;
    private criteria: ProcessInstanceHistoryCriteria = new ProcessInstanceHistoryCriteria();
    private filterValues: FilterValuesModel[];
    private processDefinitionId:string;


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
        this.loadHistoricProcessInstance();
        this.initTableAction();
        this.initTableColumns();
      }


      private loadHistoricProcessInstance(): void {

        this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
        this.camundaService.getHistoricProcessInstanceByUserId(this.userId, this.criteria, this.paginationArgs).subscribe({
          next: (response) => {
            this.totalCount = response.totalElements;
            this.processInstanceHistory = response.content.map((item: any) => ({
               id: item.instance?.id ,
               startTime: this.dateTransformPipe.transformDateAndHours(item.instance?.startTime),
               processDefinitionName:item.instance?.processDefinitionName,
               state: this.translateProcessStateToFR(item.instance?.state),
               currentTaskName: item.currentTaskName ,
               processDefinitionId: item.instance?.processDefinitionId,
               endTime: this.dateTransformPipe.transformDateAndHours(item.instance?.endTime),

            }));

          },

          error: (error) => {
            this.toastrService.onError(this.translatePipe.transform('groups.failedToLoadTasks'), this.translatePipe.transform('menu.failedToLoadTasks'));
          }
        });
      }


      private initTableAction(): void {
        this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
      }
    translateProcessStateToFR(state: string): string {
    switch (state) {
      case 'ACTIVE':
        return 'Actif';
      case 'COMPLETED':
        return 'Terminé';
      case 'SUSPENDED':
        return 'Annulé';
      default:
        return state;
    }
  }

      private initTableColumns(): void {
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.FILE_REFERENCE', 'id', false));
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.FILING_DATE', 'startTime', false));
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.PROCESS_NAME', 'processDefinitionName', false));
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.STATUS', 'state', false));
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.CURRENT_TASK', 'currentTaskName', false));
        this.tableColumns.push(new CustomTableColonneModel('historyProcessInstance.END_DATE', 'endTime', false));

      }

      public nextPage(event): void {
        this.filterValues = event.filterValues;
        this.criteria = new ProcessInstanceHistoryCriteria(
          this.filterValues.find((f) => f.columnName === 'id')?.filterValue,
          new Date(this.filterValues.find((f) => f.columnName === 'startTime')?.filterValue),
          this.filterValues.find((f) => f.columnName === 'processDefinitionName')?.filterValue,
          this.filterValues.find((f) => f.columnName === 'state')?.filterValue,
          this.filterValues.find((f) => f.columnName === 'currentTaskName')?.filterValue,
           new Date(this.filterValues.find((f) => f.columnName === 'endTime')?.filterValue),

      );
        this.page = event.pageNumber - 1;
        this.loadHistoricProcessInstance();
      }


      public onPageSizeChange(newPageSize: number): void {
        this.page = 0;
        this.pageSize = newPageSize;
        this.loadHistoricProcessInstance();
      }



    public buttonAction(event: any): void {
      console.log(event.objectClicked)
        this.camundaService.getProcessDefinitionById(event.objectClicked.processDefinitionId).subscribe({
          next: (response: ProcessDefinition) => {
            if (event.buttonActionId === 'view') {
              this.handleOnViewTask(event.objectClicked.id, response.name);
            }
          }
        });
      }

      private handleOnViewTask(processInstanceId: string, name: string): void {
        const navigationExtras: NavigationExtras = {
          state: {
            processInstanceId: processInstanceId
          }
        };
          this.router.navigate(['/pages/task-management/historic-task-list/view-historic-task/', name], navigationExtras);
      }



}
