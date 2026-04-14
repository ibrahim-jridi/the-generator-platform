import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from 'src/app/shared/models/paginationArgs.model';
import { CamundaService } from 'src/app/shared/services/camunda.service';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ProcessInstanceCriteria } from '../../../shared/models/process-instance-criteria';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { DateformatPipe } from '../../../shared/pipes/date-transform.pipe';

@Component({
  selector: 'app-process-view',
  templateUrl: './process-view.component.html',
  styleUrl: './process-view.component.scss'
})
export class ProcessViewComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected process: any[] = [];
  private task: any;
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected suspendIcon: string = 'feather icon-pause';
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected userId: string;
  protected totalElements: number;
  protected currentUserStationId: string;
  private paginationArgs: PaginationArgs;
  private processId: string;
  public state = window.history.state;
  private criteria: ProcessInstanceCriteria = new ProcessInstanceCriteria();
  private filterValues: FilterValuesModel[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private camundaService: CamundaService,
    private dateTransformPipe: DateformatPipe
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    this.processId = this.state.processId;
    if (this.processId) {
      this.loadProcess();
    }
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.filterValues = event.filterValues;
    this.criteria = new ProcessInstanceCriteria(
      this.filterValues.find((f) => f.columnName === 'id')?.filterValue,
      this.filterValues.find((f) => f.columnName === 'state')?.filterValue,
      this.filterValues.find((f) => f.columnName === 'processDefinitionId')?.filterValue,
      new Date(this.filterValues.find((f) => f.columnName === 'startTime')?.filterValue)
    );
    this.page = event.pageNumber - 1;
    this.loadProcess();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadProcess();
  }

  private loadProcess(): void {
    this.paginationArgs = {
      page: this.page,
      size: this.pageSize
    };
    this.camundaService.getProcessInstanceByCriteria(this.criteria, this.processId, this.paginationArgs).subscribe((data: any) => {
      this.totalElements = data.totalElements;
      this.process = data.content.map((item) => ({
        ...item,
        startTime: this.dateTransformPipe.transformDateAndHours(item.startTime)
      }));
    });
  }

  public redirectToAddProcess(): void {
    this.router.navigate(['/pages/bpmn-modeler']);
  }

  handleOnViewProcess(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        userId: 1, // Database id
        processInstanceId: id
      }
    };
    this.router.navigate(['view-history', label], navigationExtras);
  }

  handleOnUpdateProcess(processId: string) {
    this.router.navigate(['/pages/bpmn-modeler/', processId]);
  }
  handleOnStartProcess(id: string) {
    this.camundaService.startProcess(id).subscribe((data) => {
      console.log(data);
    });
  }
  handleOnSuspenseProcess(processDefinitionId: string) {
    this.camundaService.toggleProcessDefinitionState(processDefinitionId).subscribe((response: any) => {
      console.log(response);
      if (response == true) {
        this.suspendIcon = 'feather icon-play';
      } else {
        this.suspendIcon = 'feather icon-pause';
      }
    });
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewProcess(event.objectClicked.id, event.objectClicked.processDefinitionName);
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateProcess(event.objectClicked.id);
    }
    if (event.buttonActionId === 'play') {
      this.handleOnStartProcess(event.objectClicked.key);
    }
    if (event.buttonActionId === 'suspend') {
      this.handleOnSuspenseProcess(event.objectClicked.id);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', this.translatePipe.transform('button.VIEW'), 'btn btn-icon ', 'feather icon-eye'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('process.ID'), 'id', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('process.SUSPENSION_STATUS'), 'state', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('process.PROCESS_DEFINITION_ID'), 'processDefinitionId', false));
    this.tableColumns.push(new CustomTableColonneModel(this.translatePipe.transform('process.DATE_CREATION'), 'startTime', false));
  }

  onPageChange(page: number): void {
    this.page = page;
    this.loadProcess();
  }
}
