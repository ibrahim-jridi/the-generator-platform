import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {
  PaginationArgs,
  PaginationSortArgs,
  PaginationSortOrderType
} from '../../../shared/models/paginationArgs.model';
import {UiModalComponent} from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import {
  ButtonActionModel
} from '../../../theme/shared/components/custom-table/model/button-action.model';
import {
  CustomTableColonneModel
} from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import {CamundaService} from 'src/app/shared/services/camunda.service';
import {AppToastNotificationService} from 'src/app/shared/services/appToastNotification.service';
import {ProcessDefinition} from '../../../shared/models/processDefinition.model';
import {FileContentService} from '../../../shared/services/fileContent.service';
import {ProcessDefinitionCriteria} from '../../../shared/models/process-definition-criteria.model';
import {FilterValuesModel} from '../../../shared/models/filter-values-model';
import {TaskCriteria} from '../../../shared/models/task-criteria';
import {LoaderService} from "../../../shared/services/loader.service";

@Component({
  selector: 'app-process-list',
  templateUrl: './process-list.component.html',
  styleUrl: './process-list.component.scss'
})
export class ProcessListComponent implements OnInit {
  fileContent: string;
  protected process: ProcessDefinition[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected userId: string;
  protected totalElements: number;
  private readonly paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  private paginationArgs: PaginationArgs;
  private criteria: ProcessDefinitionCriteria = new ProcessDefinitionCriteria();
  private filterValues: FilterValuesModel[];
  public totalCount: number;
  private taskCriteria: TaskCriteria = new TaskCriteria();

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly translatePipe: TranslatePipe,
    private readonly camundaService: CamundaService,
    private readonly toastService: AppToastNotificationService,
    private readonly fileContentService: FileContentService,
    private readonly loaderService: LoaderService
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadProcess();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.criteria = new ProcessDefinitionCriteria(
      this.filterValues.find((f) => f.columnName === 'id')?.filterValue,
      this.filterValues.find((f) => f.columnName === 'name')?.filterValue,
      parseInt(this.filterValues.find((f) => f.columnName === 'version')?.filterValue),
      this.filterValues.find((f) => f.columnName === 'key')?.filterValue
    );
    this.page = event.pageNumber - 1;
    this.loadProcess();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadProcess();
  }

  public redirectToAddProcess(): void {
    this.router.navigate(['/pages/process-management/process-management-bpmn/bpmn-modeler/add']);
  }

  public uploadFile(): void {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.xml,.bpmn';

    fileInput.addEventListener('change', (event: Event) => {
      const target = event.target as HTMLInputElement;
      const file: File | undefined = target.files ? target.files[0] : undefined;
      if (file) {
        const extension = file.name.split('.').pop()?.toLowerCase();
        const isValidExtension = extension === 'xml' || extension === 'bpmn';

        if (isValidExtension) {
          const reader = new FileReader();
          reader.onload = () => {
            const fileContent = reader.result as string;
            this.fileContentService.setFileContent(fileContent);
            this.router.navigate(['bpmn-modeler/add'], { relativeTo: this.route });
          };
          reader.readAsText(file);
        } else {
          this.toastService.onError(this.translatePipe.transform('process.TYPE_OF_FILE'), this.translatePipe.transform('process.TYPE_OF_FILE'));
        }
      }
    });
    fileInput.click();
  }

  private handleOnViewProcess(id: string, name: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        processId: id
      }
    };
    this.router.navigate(['/pages/process-management/view-bpmn-modeler', name], navigationExtras);
  }

  private handleOnUpdateProcess(processId: string, name: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        processId: processId
      }
    };
    this.router.navigate(['/pages/process-management/update-bpmn-modeler/', name], navigationExtras);
  }

  private handleOnStartProcess(id: string): void {
    this.loaderService.show();
    this.camundaService.startProcessById(id).subscribe({
      next: (data) => {
        this.loaderService.hide();
        this.toastService.onSuccess(
          this.translatePipe.transform('process.SUCCESS_TO_START_PROCESS'),
          this.translatePipe.transform('process.SUCCESS_TO_START_PROCESS')
        );
        this.loadTasks();
      },
      error: (error) => {
        this.loaderService.hide();
        this.toastService.onError(
          this.translatePipe.transform('process.FAILED_TO_START_PROCESS'),
          this.translatePipe.transform('process.FAILED_TO_START_PROCESS')
        );
      }
    });
  }

  private loadTasks(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: 0, size: 5 };
    this.camundaService.getActiveTasksByAssigneeAndCriteria(this.taskCriteria, this.paginationArgs).subscribe({
      next: (response) => {
        this.totalCount = response.totalElements;
        this.camundaService.taskValidateSubject.next(this.totalCount);
      }
    });
  }

  private handleOnSuspenseProcess(processDefinitionId: string): void {
    this.camundaService.toggleProcessDefinitionState(processDefinitionId).subscribe((response: any) => {
      this.loadProcess();
    });
  }

  private handleOnExportProcess(processId: string): void {
    this.camundaService.getProcessXmlById(processId).subscribe((xml: string) => {
      if (xml) {
        const blob = new Blob([xml], { type: 'text/xml' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${processId}.xml`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    });
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewProcess(event.objectClicked.id, event.objectClicked.name);
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateProcess(event.objectClicked.id, event.objectClicked.name);
    }
    if (event.buttonActionId === 'play') {
      this.handleOnStartProcess(event.objectClicked.id);
    }
    if (event.buttonActionId === 'suspend' || event.buttonActionId === 'resume') {
      this.handleOnSuspenseProcess(event.objectClicked.id);
    }
    if (event.buttonActionId === 'download') {
      this.handleOnExportProcess(event.objectClicked.id);
    }
  }

  private loadProcess(): void {
    this.paginationArgs = {
      page: this.page,
      size: this.pageSize
    };
    this.camundaService.getDeployedProcessesByCriteria(this.criteria, this.paginationArgs).subscribe({
      next: (result) => {
        this.totalElements = result.totalElements;
        this.process = result.content;
        this.process = result.content.map((process) => ({
          ...process,
          actions: [
            { action: 'button.SUSPEND', show: !process.suspensionState },
            { action: 'button.RESUME', show: process.suspensionState },
            { action: 'button.START', show: !process.suspensionState },
            { action: 'button.LOCK', show: process.suspensionState }
          ]
        }));
      },
      error: () => {
        this.toastService.onError(this.translatePipe.transform('process.FAILED_TO_LOAD_PROCESS'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('suspend', 'button.SUSPEND', 'btn btn-icon icon-off ', 'fas fa-solid fa-power-off'));
    this.actionButton.push(new ButtonActionModel('resume', 'button.RESUME', 'btn btn-icon icon-on ', 'fas fa-solid fa-power-off'));
    this.actionButton.push(new ButtonActionModel('play', 'button.START', 'btn btn-icon', 'feather icon-play'));
    this.actionButton.push(new ButtonActionModel('lock', 'button.LOCK', 'btn btn-icon', 'feather icon-lock'));
    this.actionButton.push(new ButtonActionModel('download', 'button.EXPORT', 'btn btn-icon', 'feather icon-download'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('process.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('process.NOM', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('process.version', 'version', false));
    this.tableColumns.push(new CustomTableColonneModel('process.PROCESS_KEY', 'key', false));
  }
}
