import {Component, OnInit, ViewChild} from '@angular/core';
import {DiagramChangedEvent, ImportEvent, NgBpmnComponent} from 'libs/ng-bpmn/src';
import {CamundaService} from 'src/app/shared/services/camunda.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AppToastNotificationService} from 'src/app/shared/services/appToastNotification.service';
import {TranslatePipe} from '@ngx-translate/core';
import {FileContentService} from '../../../shared/services/fileContent.service';

@Component({
  selector: 'app-bpmn-modeler',
  templateUrl: './bpmn-modeler.component.html',
  styleUrls: ['./bpmn-modeler.component.scss']
})
export class BpmnModelerComponent implements OnInit {
  private processId: string;
  @ViewChild('bpmn', { static: false }) bpmnComponent: NgBpmnComponent;
  modeler: any;
  processData: string;
  bpmnUrl: string = '/assets/diagram.bpmn';
  private fileContent: string | null;
  public state = window.history.state;
  private aiXml: string | null = null; // add this field
  constructor(
    private camundaService: CamundaService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private fileContentService: FileContentService
  ) {}

  public ngOnInit(): void {
    this.aiXml = sessionStorage.getItem('ai_bpmn_xml');
    if (this.aiXml) {
      sessionStorage.removeItem('ai_bpmn_xml');
      sessionStorage.removeItem('ai_bpmn_session_id');
      // Override bpmnUrl so NgBpmnComponent doesn't auto-load the blank diagram
      const blob = new Blob([this.aiXml], { type: 'text/xml' });
      this.bpmnUrl = URL.createObjectURL(blob);
    } else {
      this.initialize();
    }
  }
  public ngAfterViewInit(): void {
    if (this.aiXml) {
      setTimeout(() => this.loadBpmnModel(this.aiXml!), 100);
    }
  }

  private initialize(): void {
    this.processId = this.state.processId;
    this.fileContent = this.fileContentService.getFileContent();
    if (this.processId) {
      this.loadProcessById(this.processId);
    } else if (this.fileContent) {
      const blob = new Blob([this.fileContent], { type: 'text/xml' });
      this.bpmnUrl = URL.createObjectURL(blob);
      this.loadBpmnModel(this.fileContent);
    }
  }

  public handleImported(event: ImportEvent): void {
    const { type } = event;

    if (type === 'success') {
      this.toastService.onSuccess(
        this.translatePipe.transform('process.BPMN_UPLOADED_SUCCESSFULLY'),
        this.translatePipe.transform('process.BPMN_UPLOADED_SUCCESSFULLY')
      );
    } else if (type === 'error') {
      this.toastService.onError(
        this.translatePipe.transform('process.BPMN_UPLOAD_FAILED'),
        this.translatePipe.transform('process.BPMN_UPLOADED_FAILED')
      );
    }
  }

  public onChanged($event: DiagramChangedEvent): void {}

  async getProcessName(): Promise<string> {
    if (!this.bpmnComponent) {
      return 'Unknown Process';
    }

    try {
      const result = await this.bpmnComponent.saveXML();
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(result, 'text/xml');
      const processElement = xmlDoc.getElementsByTagName('bpmn:process')[0];
      return processElement ? processElement.getAttribute('name') || 'Unknown Process' : 'Unknown Process';
    } catch (error) {
      console.error('Error retrieving process name:', error);
      return 'Unknown Process';
    }
  }

  async getProcessId(): Promise<string> {
    if (!this.bpmnComponent) {
      return 'Unknown Process ID';
    }

    try {
      const result = await this.bpmnComponent.saveXML();
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(result, 'text/xml');
      const processElement = xmlDoc.getElementsByTagName('bpmn:process')[0];
      return processElement ? processElement.getAttribute('id') || 'Unknown Process ID' : 'Unknown Process ID';
    } catch (error) {
      console.error('Error retrieving process ID:', error);
      return 'Unknown Process ID';
    }
  }

  async getProcessXml(): Promise<string> {
    if (!this.bpmnComponent) {
      return 'Unknown Process';
    }

    try {
      const result = await this.bpmnComponent.saveXML();
      return result || 'Unknown Process';
    } catch (error) {
      console.error('Error retrieving process name:', error);
      return 'Unknown Process';
    }
  }

  getCurrentDate(): string {
    const date = new Date();
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
  }

  async saveProcessData() {
    try {
      const processName = await this.getProcessName();
      const processXml = await this.getProcessXml();
      const processId = await this.getProcessId();
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(processXml, 'application/xml');

      const namespaceResolver = (prefix) => {
        if (prefix === 'bpmn') {
          return 'http://www.omg.org/spec/BPMN/20100524/MODEL';
        }
        return null;
      };
      const processExpression = '//bpmn:process';
      const processResult = xmlDoc.evaluate(processExpression, xmlDoc, namespaceResolver, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
      const processElement = processResult.singleNodeValue as Element;

      if (processElement) {
        const historyTimeToLiveAttr = processElement.getAttribute('camunda:historyTimeToLive');
        const nameAttr = processElement.getAttribute('name');
        if (!nameAttr || nameAttr === '') {
          this.toastService.onError(
            this.translatePipe.transform('process.BPMN_NAME_REQUIRED'),
            this.translatePipe.transform('process.FAILED_TO_SAVE_PROCESS')
          );
          return;
        }
        if (!historyTimeToLiveAttr || historyTimeToLiveAttr === '') {
          this.toastService.onError(
            this.translatePipe.transform('process.HISTORY_TIME_TO_LIVE_REQUIRED'),
            this.translatePipe.transform('process.PROCESS_WITHOUT_HISTORY_TIME_TO_LIVE')
          );
          return;
        }
      }

      const xpathExpression = '//bpmn:userTask';
      const xpathResult = xmlDoc.evaluate(xpathExpression, xmlDoc, namespaceResolver, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);

      for (let i = 0; i < xpathResult.snapshotLength; i++) {
        const userTask = xpathResult.snapshotItem(i) as Element;
        const formKeyAttr = userTask.getAttribute('camunda:formKey');

        if (!formKeyAttr || formKeyAttr === '') {
          this.toastService.onError(
            this.translatePipe.transform('process.FORM_KEY_REQUIRED'),
            this.translatePipe.transform('process.USER_TASK_WITHOUT_FORM_KEY')
          );
          return;
        }
      }

      const xCallExpression = '//bpmn:callActivity';
      const xCallResult = xmlDoc.evaluate(xCallExpression, xmlDoc, namespaceResolver, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);

      for (let i = 0; i < xCallResult.snapshotLength; i++) {
        const callTask = xCallResult.snapshotItem(i) as Element;
        const calledElementAttr = callTask.getAttribute('calledElement');
        console.log(calledElementAttr);
        if (!calledElementAttr || calledElementAttr === '') {
          this.toastService.onError(this.translatePipe.transform('process.CALLED_ELEMENT'), this.translatePipe.transform('process.CALLED_ELEMENT'));
          return;
        }
      }

      const processData = {
        name: processName,
        date: this.getCurrentDate(),
        xml: processXml
      };

      this.camundaService.deployProcessDefinition(processData).subscribe({
        next: (response) => {
          this.toastService.onSuccess(
            this.translatePipe.transform('process.SUCCESS_TO_SAVE_PROCESS'),
            this.translatePipe.transform('process.SUCCESS_TO_SAVE_PROCESS')
          );
          this.router.navigate(['/pages/process-management/process-management-bpmn']);
        },
        error: (error) => {
          console.error('Error saving process data:', error);
          this.toastService.onError(
            this.translatePipe.transform('process.FAILED_TO_SAVE_PROCESS'),
            this.translatePipe.transform('process.FAILED_TO_SAVE_PROCESS')
          );
        }
      });
    } catch (error) {
      console.error('Error saving process data:', error);
    }
  }

  loadProcessById(processId: string): void {
    this.camundaService.getProcessXmlById(processId).subscribe({
      next: (processXml) => {
        this.processData = processXml;

        const blob = new Blob([processXml], { type: 'text/xml' });
        this.bpmnUrl = URL.createObjectURL(blob);

        this.loadBpmnModel(processXml);
      },
      error: (error) => {
        console.error('Failed to load process XML:', error);
      },
      complete: () => {}
    });
  }

  loadBpmnModel(xml: string) {
    this.bpmnComponent.importDiagram(xml);
  }

  protected cancel(): void {
    this.router.navigate(['/pages/process-management/process-management-bpmn']);
  }
}
