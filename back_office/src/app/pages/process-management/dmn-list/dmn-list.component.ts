import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { CamundaService } from 'src/app/shared/services/camunda.service';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { DmnActivityDecisionDefinition } from '../../../shared/models/dmn-activity-decision-definition.model';
import { FileContentService } from '../../../shared/services/fileContent.service';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { DecisionDefinitionCriteria } from '../../../shared/models/decision-definition-criteria';
import { ProcessDefinitionCriteria } from '../../../shared/models/process-definition-criteria.model';
import { PaginationArgs } from '../../../shared/models/paginationArgs.model';

@Component({
  selector: 'app-dmn-list',
  templateUrl: './dmn-list.component.html',
  styleUrl: './dmn-list.component.scss'
})
export class DmnListComponent implements OnInit {
  protected dmnProcess: DmnActivityDecisionDefinition[] = [];
  protected pageSize: number;
  protected page: number = 0;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected userId: string;
  protected totalElements: number;
  private paginationArgs: PaginationArgs;
  private criteria: DecisionDefinitionCriteria = new DecisionDefinitionCriteria();
  private filterValues: FilterValuesModel[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private camundaService: CamundaService,
    private toastService: AppToastNotificationService,
    private fileContentService: FileContentService
  ) {}

  public ngOnInit(): void {
    this.pageSize = 5;
    this.initTableColumns();
    this.initTableAction();
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
    this.loadDmnProcess();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.page = 0;
    this.pageSize = newPageSize;
    this.loadDmnProcess();
  }

  public redirectToAddDmn(): void {
    this.router.navigate(['/pages/process-management/process-management-dmn/dmn-modeler/add']);
  }

  public uploadFile(): void {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.xml,.dmn';

    fileInput.addEventListener('change', (event: Event) => {
      const target = event.target as HTMLInputElement;
      const file: File | undefined = target.files ? target.files[0] : undefined;
      if (file) {
        const extension = file.name.split('.').pop()?.toLowerCase();
        const isValidExtension = extension === 'xml' || extension === 'dmn';

        if (isValidExtension) {
          const reader = new FileReader();
          reader.onload = () => {
            const fileContent = reader.result as string;
            this.fileContentService.setFileContent(fileContent);
            this.router.navigate(['dmn-modeler/add'], { relativeTo: this.route });
          };
          reader.readAsText(file);
        } else {
          this.toastService.onError(
            this.translatePipe.transform('process.TYPE_OF_DMN_FILE'),
            this.translatePipe.transform('process.TYPE_OF_DMN_FILE')
          );
        }
      }
    });
    fileInput.click();
  }

  handleOnUpdateProcessDmn(processDmnId: string, name: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        processDmnId: processDmnId
      }
    };
    this.router.navigate(['/pages/process-management/update-dmn-modeler/', name], navigationExtras);
  }

  public handleOnExportProcessDmn(processDmnId: string): void {
    this.camundaService.getDecisionDefinitionById(processDmnId).subscribe((xml: string) => {
      if (xml) {
        const blob = new Blob([xml], { type: 'text/xml' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${processDmnId}.xml`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    });
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateProcessDmn(event.objectClicked.id, event.objectClicked.name);
    }
    if (event.buttonActionId === 'download') {
      this.handleOnExportProcessDmn(event.objectClicked.id);
    }
  }

  private loadDmnProcess(): void {
    this.paginationArgs = {
      page: this.page,
      size: this.pageSize
    };
    this.camundaService.getAllDecisionDefinitionByCriteria(this.criteria, this.paginationArgs).subscribe((data) => {
      this.dmnProcess = data.content;
      this.totalElements = data.totalElements;
    });
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('download', 'button.EXPORT', 'btn btn-icon', 'feather icon-download'));
  }

  private initTableColumns(): void {
    this.loadDmnProcess();
    this.tableColumns.push(new CustomTableColonneModel('process.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('process.DMN_NAME', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('process.version', 'version', false));
    this.tableColumns.push(new CustomTableColonneModel('process.DMN_KEY', 'key', false));
  }
}

