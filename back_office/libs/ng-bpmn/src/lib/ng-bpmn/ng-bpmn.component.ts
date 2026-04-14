import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { from, map, Observable, of, Subscription, switchMap } from 'rxjs';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import Canvas from 'diagram-js/lib/core/Canvas';
import { BpmnPropertiesPanelModule, BpmnPropertiesProviderModule } from 'bpmn-js-properties-panel';
import MinimapModule from 'diagram-js-minimap';
import AddExporter from '@bpmn-io/add-exporter';
import { EditorActions } from '../core/modeling/EditorActions';
import { Modeler } from '../core/Modeler';
import { ModelerComponent } from '../core/ModelerComponent';
import { ModelerActions } from '../core/modeling/ModelerActions';
import BpmnActionsModule from '../core/modeling/BpmnActionsModule/index';
import { DiagramMinimap } from '../core/modeling/DiagramMinimap';
import { debounce } from '../utils/debounce';
import { ImportEvent } from '../core/ImportEvent';
import { exporter } from '../core/exporter';
import { ImportCallback } from '../core/ImportCallback';
import DiagramActionsModule from '../core/modeling/DiagramActionsModule/index';
import processPropertiesProviderModule from '../core/modeling/provider/process/index';
import camundaModdleDescriptor from '../core/modeling/descriptors/camunda.json';
import eventPropertiesProviderModule from '../core/modeling/provider/Event/index';
import userTaskPropertiesProviderModule from '../core/modeling/provider/userTask/index';
import callActivityPropertiesProviderModule from '../core/modeling/provider/callActivity/index';
import serviceTaskPropertiesProviderModule from '../core/modeling/provider/serviceTask/index';
import businessRuleTaskPropertiesProviderModule from '../core/modeling/provider/businessRuleTask/index';
import sequenceFlowPropertiesProviderModule from '../core/modeling/provider/SequenceFlow/index';
import ColorPickerModule from '../core/modeling/BpmnColorPickerModule/index';
import { User } from '../../../../../src/app/shared/models/user.model';
import UserAssignmentProvider from '../core/modeling/provider/userTask/parts/UserAssignment';
import { FormService } from '../../../../../src/app/shared/services/form.service';
import { Form } from '../../../../../src/app/shared/models/form.model';
import FormsProvider from '../core/modeling/provider/Event/parts/Forms';
import sendTaskPropertiesProviderModule from '../core/modeling/provider/sendTask/index';
import NotificationProvider from '../core/modeling/provider/sendTask/parts/Notification';
import CallActivityListProvider from '../core/modeling/provider/callActivity/parts/CallActivityList';
import { GroupsService } from '../../../../../src/app/shared/services/groups.service';
import { RoleService } from '../../../../../src/app/shared/services/role.service';
import { Group } from '../../../../../src/app/shared/models/group.model';
import { Role } from '../../../../../src/app/shared/models/role.model';
import { UserService } from '../../../../../src/app/shared/services/user.service';
import { Template } from '../../../../../src/app/shared/models/template.model';
import { ReportService } from '../../../../../src/app/shared/services/report.service';
import TemplatesProvider from '../core/modeling/provider/businessRuleTask/parts/ImplementationListProps';
import { CamundaService } from '../../../../../src/app/shared/services/camunda.service';
import { DmnActivityDecisionDefinition } from '../../../../../src/app/shared/models/dmn-activity-decision-definition.model';
import multiInstanceProviderModule from '../core/modeling/provider/Task/index'
import { Notification } from '../../../../../src/app/shared/models/notification.model';
import { NotificationService } from '../../../../../src/app/shared/services/notification.service';
export interface DiagramChangedEvent {
  xml?: string;
  error?: Error;
}

@Component({
  selector: 'ng-bpmn',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './ng-bpmn.component.html',
  styleUrls: ['./ng-bpmn.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NgBpmnComponent extends ModelerComponent implements Modeler, OnInit, OnChanges, OnDestroy {
  public bpmnJS?: BpmnModeler;
  @Input({ required: true }) url?: string;
  @Input() showProperties = false;
  @Input() showMinimap = false;
  @Input() autoOpenMinimap = false;
  @Input() hotkeys = false;

  @ViewChild('canvas', { static: true })
  private canvas?: ElementRef;

  @ViewChild('properties', { static: true })
  private properties?: ElementRef;

  @Output()
  importDone = new EventEmitter<ImportEvent>();

  @Output()
  changed = new EventEmitter<DiagramChangedEvent>();


  private CAMUNDA_NAMESPACE = 'http://camunda.org/schema/1.0/bpmn';
  private BPMN_NAMESPACE = 'http://www.omg.org/spec/BPMN/20100524/MODEL';

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private formService: FormService,
    private groupService: GroupsService,
    private roleService: RoleService,
    private reportService: ReportService,
    private camundaService: CamundaService,
    private notificationService:NotificationService
  ) {
    super();
  }

  get editorActions(): EditorActions | undefined {
    const editorActions = this.bpmnJS?.get<EditorActions>('editorActions');
    return editorActions;
  }

  async ngOnInit(): Promise<void> {
    let users: User[];
    let forms: Form[];
    let decisionTables: DmnActivityDecisionDefinition[];
    let processDefinitions: any;
    let processDefinitionResponse: any;
    let groupList: Group[];
    let roles: Role[];
    let templates: Template[];
    let notifications:Notification[];
    const additionalModules = [
      AddExporter,
      BpmnPropertiesPanelModule,
      BpmnPropertiesProviderModule,
      DiagramActionsModule,
      BpmnActionsModule,
      ColorPickerModule,
      processPropertiesProviderModule,
      eventPropertiesProviderModule,
      userTaskPropertiesProviderModule,
      serviceTaskPropertiesProviderModule,
      businessRuleTaskPropertiesProviderModule,
      sequenceFlowPropertiesProviderModule,
      sendTaskPropertiesProviderModule,
      callActivityPropertiesProviderModule,
      multiInstanceProviderModule
    ];

    if (this.showMinimap) {
      additionalModules.push(MinimapModule);
    }

    const modeler = new BpmnModeler({
      exporter,
      container: this.canvas?.nativeElement,
      propertiesPanel: {
        parent: this.properties?.nativeElement
      },
      additionalModules,
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    });

    if (this.showMinimap && this.autoOpenMinimap) {
      modeler.get<DiagramMinimap>('minimap').open();
    }

    modeler.on('import.done', ({ error }: ImportCallback) => {
      if (!error && this.bpmnJS) {
        const canvas = this.bpmnJS.get<Canvas>('canvas');
        canvas.zoom('fit-viewport');
      }
    });

    const onChanged = debounce(async () => {
      try {
        const content = await this.bpmnJS?.saveXML();
        if (content) {
          const modifiedXML = this.transformXML(content.xml);
          this.changed.next({ xml: modifiedXML });
        }
      } catch (err) {
        console.error(err);
      }
    });

    modeler.on('commandStack.changed', onChanged);
    modeler.on('import.done', onChanged);

    this.bpmnJS = modeler;

    users = await this.userService.getAllUsers();


    groupList = await this.groupService.getGroupsList();
    roles = await this.roleService.getRoles();

    try {
      users = await this.userService.getAllUsers();
      console.log('1 ✅ users:', users?.length);
    } catch (e) { console.error('1 ❌ users FAILED:', e); users = []; }

    const propertiesPanel = modeler.get('propertiesPanel') as any;

    try {
      groupList = await this.groupService.getGroupsList();
      console.log('2 ✅ groups:', groupList?.length);
    } catch (e) { console.error('2 ❌ groups FAILED:', e); groupList = []; }

    try {
      roles = await this.roleService.getRoles();
      console.log('3 ✅ roles:', roles?.length);
    } catch (e) { console.error('3 ❌ roles FAILED:', e); roles = []; }

    try {
      processDefinitionResponse = await this.camundaService.getDeployedProcessesAsPromise();
      processDefinitions = processDefinitionResponse?.content;
      console.log('4 ✅ processDefs:', processDefinitions?.length);
    } catch (e) { console.error('4 ❌ processDefs FAILED:', e); processDefinitions = []; }

    propertiesPanel.registerProvider(new CallActivityListProvider(processDefinitions));
    propertiesPanel.registerProvider(new UserAssignmentProvider(users, groupList, roles));

    try {
      forms = await this.formService.getAllForms();
      console.log('5 ✅ forms:', forms?.length);
    } catch (e) { console.error('5 ❌ forms FAILED:', e); forms = []; }

    propertiesPanel.registerProvider(new FormsProvider(forms));

    try {
      templates = await this.reportService.getTemplatesAsPromise();
      console.log('6 ✅ templates:', templates?.length);
    } catch (e) { console.error('6 ❌ templates FAILED:', e); templates = []; }

    try {
      decisionTables = await this.camundaService.getAllDecisionDefinitionAsPromise();
      console.log('7 ✅ decisionTables:', decisionTables?.length);
    } catch (e) { console.error('7 ❌ decisionTables FAILED:', e); decisionTables = []; }

    try {
      notifications = await this.notificationService.getNotificationAsPromise();
      console.log('8 ✅ notifications:', notifications?.length);
    } catch (e) { console.error('8 ❌ notifications FAILED:', e); notifications = []; }

    propertiesPanel.registerProvider(new TemplatesProvider(templates, decisionTables));
    propertiesPanel.registerProvider(new NotificationProvider(users, groupList, roles, notifications));

    if (this.url) {
      this.loadUrl(this.url);
    }

  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['url']) {
      this.loadUrl(changes['url'].currentValue);
    }
  }

  ngOnDestroy(): void {
    if (this.hotkeys) {
      this.unbindHotkeys();
    }
    this.bpmnJS?.destroy();
  }

  private onLoad() {
    if (this.hotkeys) {
      this.bindHotkeys();
    }
  }

  toggleProperties() {
    this.showProperties = !this.showProperties;
  }

  loadUrl(url: string): Subscription {
    return this.http
      .get(url, { responseType: 'text' })
      .pipe(
        switchMap((xml: string) => this.importDiagram(xml)),
        map((result) => result.warnings)
      )
      .subscribe({
        next: (warnings) => {
          this.importDone.emit({
            type: 'success',
            warnings
          });

          this.onLoad();
        },
        error: (err: HttpErrorResponse) => {
          this.importDone.emit({
            type: 'error',
            error: err
          });
        }
      });
  }

  async saveXML(): Promise<string | undefined> {
    if (this.bpmnJS) {
      const { xml } = await this.bpmnJS.saveXML({ format: true });
      return this.transformXML(xml);
    } else {
      return Promise.reject('Modeler not initialized');
    }
  }

  async saveSVG(): Promise<string | undefined> {
    if (this.bpmnJS) {
      const { svg } = await this.bpmnJS.saveSVG();
      return svg;
    } else {
      return Promise.reject('Modeler not initialized');
    }
  }

  public importDiagram(xml: string): Observable<{ warnings: Array<string> }> {
    if (this.bpmnJS) {
      return from(this.bpmnJS.importXML(xml));
    } else {
      return of({ warnings: [] });
    }
  }

  private transformXML(xml: string): string {
    xml = this.removeCamundaTypeFormAttribute(xml);

    xml = this.updateCamundaTypeAttribute(xml);

    const parser = new DOMParser();
    const serializer = new XMLSerializer();
    const doc = parser.parseFromString(xml, 'application/xml');

    function transformJobExecution(elementType: string) {
      const startEvents = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);
      for (let i = 0; i < startEvents.length; i++) {
        const startEvent = startEvents[i];
        const failedJobRetryTimeCycle = startEvent.getAttribute('camunda:failedJobRetryTimeCycle');
        if (failedJobRetryTimeCycle) {
          let extensionElements = startEvent.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];
          if (!extensionElements) {
            extensionElements = doc.createElementNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'bpmn:extensionElements');
            startEvent.appendChild(extensionElements);
          }

          const failedJobRetryTimeCycleElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:failedJobRetryTimeCycle');
          failedJobRetryTimeCycleElement.textContent = failedJobRetryTimeCycle;
          extensionElements.appendChild(failedJobRetryTimeCycleElement);

          startEvent.removeAttribute('camunda:failedJobRetryTimeCycle');
        }
        if (startEvent.hasAttribute('camunda:exclusive')) {
          startEvent.removeAttribute('camunda:exclusive');
        }
      }
    }

    const participants = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'participant');
    for (let i = 0; i < participants.length; i++) {
      const participant = participants[i];
      const historyTimeToLive = participant.getAttribute('camunda:historyTimeToLive');
      if (historyTimeToLive) {
        const processRef = participant.getAttribute('processRef');
        if (processRef) {
          const processes = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'process');
          for (let j = 0; j < processes.length; j++) {
            const process = processes[j];
            if (process.getAttribute('id') === processRef) {
              process.setAttribute('camunda:historyTimeToLive', historyTimeToLive);
              break;
            }
          }
        }
        participant.removeAttribute('camunda:historyTimeToLive');
      }
    }

    function transformExecutionListeners(elementType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        const extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];

        if (extensionElements) {
          const executionListeners = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'executionListner');

          for (let j = 0; j < executionListeners.length; j++) {
            const executionListener = executionListeners[j];
            const parameterExecutions = executionListener.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'parameterExecution');

            for (let k = 0; k < parameterExecutions.length; k++) {
              const parameterExecution = parameterExecutions[k];

              const newExecutionListener = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:executionListener');

              const event = parameterExecution.getAttribute('event');
              const clazz = parameterExecution.getAttribute('class');
              const expression = parameterExecution.getAttribute('expression');
              const scriptFormat = parameterExecution.getAttribute('scriptFormat');
              const delegateExpression = parameterExecution.getAttribute('delegateExpression');
              const type = parameterExecution.getAttribute('type');
              const script = parameterExecution.getAttribute('script');
              const resource = parameterExecution.getAttribute('resource');
              const listnerType = parameterExecution.getAttribute('listnerType');

              if (event) newExecutionListener.setAttribute('event', event);
              if (clazz) newExecutionListener.setAttribute('class', clazz);
              if (expression) newExecutionListener.setAttribute('expression', expression);
              if (scriptFormat) newExecutionListener.setAttribute('scriptFormat', scriptFormat);
              if (delegateExpression) newExecutionListener.setAttribute('delegateExpression', delegateExpression);

              if (listnerType === 'script') {
                const scriptElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${listnerType}`);
                if (resource) {
                  scriptElement.setAttribute('resource', resource);
                }
                if (script) {
                  scriptElement.textContent = script;
                }
                newExecutionListener.appendChild(scriptElement);
              }

              const extensions = parameterExecution.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'extension');
              for (let m = 0; m < extensions.length; m++) {
                const extension = extensions[m];
                const name = extension.getAttribute('name');
                const value = extension.getAttribute('value');
                const type = extension.getAttribute('type');

                if (name && (value || type || expression)) {
                  const newField = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:field');
                  newField.setAttribute('name', name);

                  const typeElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${type}`);
                  typeElement.textContent = value;

                  newField.appendChild(typeElement);
                  newExecutionListener.appendChild(newField);
                }
              }
              extensionElements.appendChild(newExecutionListener);
            }
            extensionElements.removeChild(executionListener);
          }
        }
      }
    }

    function transformCamundaFields(elementType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        const extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];

        if (extensionElements) {
          const camundaFields = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'fields');

          for (let j = 0; j < camundaFields.length; j++) {
            const field = camundaFields[j];
            const camundaField = field.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'field')[0];

            if (camundaField) {
              const name = camundaField.getAttribute('name');
              const value = camundaField.getAttribute('value');
              const type = camundaField.getAttribute('type');

              if (name && value && type) {
                const newField = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:field');
                newField.setAttribute('name', name);

                const typeElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${type}`);
                typeElement.textContent = value;
                newField.appendChild(typeElement);

                extensionElements.replaceChild(newField, field);
              }
            }
          }
        }
      }
    }

    function transformCamundaInputParameters(elementType: string, parameterType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        const extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];

        if (extensionElements) {
          const inputOutputs = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'inputOutput');

          for (let j = 0; j < inputOutputs.length; j++) {
            const inputOutput = inputOutputs[j];
            const inputParameters = inputOutput.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', parameterType);

            for (let k = 0; k < inputParameters.length; k++) {
              const inputParameter = inputParameters[k];
              const name = inputParameter.getAttribute('name');
              const scriptFormat = inputParameter.getAttribute('scriptFormat');
              const script = inputParameter.getAttribute('script');
              const assignmentType = inputParameter.getAttribute('assignmentType');
              const expression = inputParameter.getAttribute('expression');
              const resource = inputParameter.getAttribute('resource');

              if (name && assignmentType && (script || expression || scriptFormat || resource)) {
                const newInputParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${parameterType}`);
                newInputParameter.setAttribute('name', name);
                if (assignmentType == 'stringExpression') {
                  if (expression) {
                    newInputParameter.textContent = expression;
                  }
                } else {
                  const scriptElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${assignmentType}`);
                  if (scriptFormat) {
                    scriptElement.setAttribute('scriptFormat', scriptFormat);
                  }
                  if (script) {
                    scriptElement.textContent = script;
                  }

                  if (resource) {
                    scriptElement.setAttribute('resource', resource);
                  }

                  newInputParameter.appendChild(scriptElement);
                }
                inputOutput.replaceChild(newInputParameter, inputParameter);
              }
            }
          }
        }
      }
    }

    function transformBusinessRuleTasks(elementType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        const camundaType = element.getAttribute('camunda:type');
        const extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];

        if (camundaType === 'connector' && extensionElements) {
          const camundaConnector = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:connector');

          const inputOutputConnectors = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'inputOutputConnector');
          for (let j = 0; j < inputOutputConnectors.length; j++) {
            const inputOutputConnector = inputOutputConnectors[j];
            const camundaInputOutput = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputOutput');

            const inputParameters = inputOutputConnector.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'inputParameter');
            for (let k = 0; k < inputParameters.length; k++) {
              const inputParameter = inputParameters[k];
              const name = inputParameter.getAttribute('name');
              const scriptFormat = inputParameter.getAttribute('scriptFormat');
              const script = inputParameter.getAttribute('script');
              const assignmentType = inputParameter.getAttribute('assignmentType');
              const expression = inputParameter.getAttribute('expression');
              const resource = inputParameter.getAttribute('resource');

              if (name && assignmentType && (script || expression || scriptFormat || resource)) {
                const newInputParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputParameter');
                newInputParameter.setAttribute('name', name);
                if (assignmentType === 'stringExpression') {
                  if (expression) {
                    newInputParameter.textContent = expression;
                  }
                } else {
                  const scriptElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${assignmentType}`);
                  if (scriptFormat) {
                    scriptElement.setAttribute('scriptFormat', scriptFormat);
                  }
                  if (script) {
                    scriptElement.textContent = script;
                  }
                  if (resource) {
                    scriptElement.setAttribute('resource', resource);
                  }
                  newInputParameter.appendChild(scriptElement);
                }
                camundaInputOutput.appendChild(newInputParameter);
              }
            }

            const outputParameters = inputOutputConnector.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'outputParameter');
            for (let k = 0; k < outputParameters.length; k++) {
              const outputParameter = outputParameters[k];
              const name = outputParameter.getAttribute('name');
              const scriptFormat = outputParameter.getAttribute('scriptFormat');
              const script = outputParameter.getAttribute('script');
              const assignmentType = outputParameter.getAttribute('assignmentType');
              const expression = outputParameter.getAttribute('expression');
              const resource = outputParameter.getAttribute('resource');

              if (name && assignmentType && (script || expression || scriptFormat || resource)) {
                const newOutputParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:outputParameter');
                newOutputParameter.setAttribute('name', name);
                if (assignmentType === 'stringExpression') {
                  if (expression) {
                    newOutputParameter.textContent = expression;
                  }
                } else {
                  const scriptElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${assignmentType}`);
                  if (scriptFormat) {
                    scriptElement.setAttribute('scriptFormat', scriptFormat);
                  }
                  if (script) {
                    scriptElement.textContent = script;
                  }
                  if (resource) {
                    scriptElement.setAttribute('resource', resource);
                  }
                  newOutputParameter.appendChild(scriptElement);
                }
                camundaInputOutput.appendChild(newOutputParameter);
              }
            }

            camundaConnector.appendChild(camundaInputOutput);
            extensionElements.removeChild(inputOutputConnector);
          }

          const connectorId = element.getAttribute('camunda:connectorId');
          const connectorIdElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:connectorId');
          connectorIdElement.textContent = connectorId;
          camundaConnector.appendChild(connectorIdElement);

          extensionElements.appendChild(camundaConnector);

          element.removeAttribute('camunda:type');
          element.removeAttribute('camunda:connectorId');
        }
      }
    }

    function removeElementsByCamundaType(typeValue: string): void {
      const BPMN_NAMESPACE = 'http://www.omg.org/spec/BPMN/20100524/MODEL';
      const CAMUNDA_NAMESPACE = 'http://camunda.org/schema/1.0/bpmn';

      const elements = doc.getElementsByTagNameNS(BPMN_NAMESPACE, '*');

      for (let i = elements.length - 1; i >= 0; i--) {
        const element = elements[i];
        const camundaType = element.getAttributeNS(CAMUNDA_NAMESPACE, 'type');

        if (camundaType === typeValue) {
          element.removeAttributeNS(CAMUNDA_NAMESPACE, 'type');
        }
      }
    }

    function transformSequenceFlows() {
      const sequenceFlows = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'sequenceFlow');
      for (let i = 0; i < sequenceFlows.length; i++) {
        const sequenceFlow = sequenceFlows[i];
        const camundaType = sequenceFlow.getAttribute('camunda:type');
        const camundaLanguage = sequenceFlow.getAttribute('camunda:language');
        const camundaScript = sequenceFlow.getAttribute('camunda:script');
        const resource = sequenceFlow.getAttribute('camunda:resource');
        const expression = sequenceFlow.getAttribute('camunda:expression');

        if (camundaType || camundaType == '') {
          sequenceFlow.removeAttribute('camunda:type');
        }
        if (sequenceFlow.hasAttribute('camunda:typeScript')) {
          sequenceFlow.removeAttribute('camunda:typeScript');
        }

        const conditionExpression = sequenceFlow.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'conditionExpression')[0];
        if (conditionExpression) {
          if (camundaLanguage) {
            conditionExpression.setAttribute('language', camundaLanguage);
            sequenceFlow.removeAttribute('camunda:language');
          }
          if (resource) {
            conditionExpression.setAttribute('resource', resource);
            sequenceFlow.removeAttribute('camunda:resource');
          }
          if (camundaScript) {
            conditionExpression.textContent = camundaScript;
            sequenceFlow.removeAttribute('camunda:script');
          }
          if (expression) {
            conditionExpression.textContent = expression;
          //  sequenceFlow.removeAttribute('camunda:expression');
          }
        }
      }
    }

    function addMessageDetails(elementType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];

        const sendTo = element.getAttribute('camunda:sendTo');
        const messageSubject = element.getAttribute('camunda:messageSubject');
        const messageBody = element.getAttribute('camunda:messageBody');

        let extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];
        if (!extensionElements) {
          extensionElements = doc.createElementNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'bpmn:extensionElements');

          const incomingElement = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'incoming')[0];
          const outgoingElement = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'outgoing')[0];

          if (incomingElement) {
            element.insertBefore(extensionElements, incomingElement);
          } else if (outgoingElement) {
            element.insertBefore(extensionElements, outgoingElement);
          } else {
            element.appendChild(extensionElements);
          }
        }

        let camundaInputOutput = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'inputOutput')[0];
        if (!camundaInputOutput) {
          camundaInputOutput = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputOutput');
          extensionElements.appendChild(camundaInputOutput);
        }

        let sendToParameter = camundaInputOutput.querySelector('[name="sendTo"]');
        if (!sendToParameter) {
          sendToParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputParameter');
          sendToParameter.setAttribute('name', 'sendTo');
          camundaInputOutput.appendChild(sendToParameter);
        }
        sendToParameter.textContent = sendTo;

        let messageSubjectParameter = camundaInputOutput.querySelector('[name="messageSubject"]');
        if (!messageSubjectParameter) {
          messageSubjectParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputParameter');
          messageSubjectParameter.setAttribute('name', 'messageSubject');
          camundaInputOutput.appendChild(messageSubjectParameter);
        }
        messageSubjectParameter.textContent = messageSubject;

        let messageBodyParameter = camundaInputOutput.querySelector('[name="messageBody"]');
        if (!messageBodyParameter) {
          messageBodyParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:inputParameter');
          messageBodyParameter.setAttribute('name', 'messageBody');
          camundaInputOutput.appendChild(messageBodyParameter);
        }
        messageBodyParameter.textContent = messageBody;
      }
    }

    function transformMultiInstanceLoopCharacteristics() {
      const multiInstanceElements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'multiInstanceLoopCharacteristics');

      for (let i = 0; i < multiInstanceElements.length; i++) {
        const multiInstanceElement = multiInstanceElements[i];

        const failedJobRetryTimeCycle = multiInstanceElement.getAttribute('camunda:failedJobRetryTimeCycle');
        if (failedJobRetryTimeCycle) {
          let extensionElements = multiInstanceElement.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];
          if (!extensionElements) {
            extensionElements = doc.createElementNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'bpmn:extensionElements');
            multiInstanceElement.appendChild(extensionElements);
          }

          const failedJobRetryTimeCycleElement = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', 'camunda:failedJobRetryTimeCycle');
          failedJobRetryTimeCycleElement.textContent = failedJobRetryTimeCycle;
          extensionElements.appendChild(failedJobRetryTimeCycleElement);

          multiInstanceElement.removeAttribute('camunda:failedJobRetryTimeCycle');
        }

        if (multiInstanceElement.hasAttribute('camunda:exclusive')) {
          multiInstanceElement.removeAttribute('camunda:exclusive');
        }
      }
    }

    const addCallActivityDetails = (elementType) => {
      const elements = doc.getElementsByTagNameNS(this.BPMN_NAMESPACE, elementType);
      for (const element of Array.from(elements)) {
        const inVariablesCheck = element.getAttribute('camunda:inVariablesCheck');
        const outVariablesCheck = element.getAttribute('camunda:outVariablesCheck');
        const inLocalCheck = element.getAttribute('camunda:inLocalCheck');
        const outLocalCheck = element.getAttribute('camunda:outLocalCheck');

        let extensionElements = element.getElementsByTagNameNS(this.BPMN_NAMESPACE, 'extensionElements')[0];

        if (inVariablesCheck || outVariablesCheck) {
          if (!extensionElements) {
            extensionElements = doc.createElementNS(this.BPMN_NAMESPACE, 'bpmn:extensionElements');
            const incomingElement = element.getElementsByTagNameNS(this.BPMN_NAMESPACE, 'incoming')[0];
            const outgoingElement = element.getElementsByTagNameNS(this.BPMN_NAMESPACE, 'outgoing')[0];
            if (incomingElement) {
              element.insertBefore(extensionElements, incomingElement);
            } else if (outgoingElement) {
              element.insertBefore(extensionElements, outgoingElement);
            } else {
              element.appendChild(extensionElements);
            }
          }
        }


        if (outVariablesCheck === "true") {
          let camundaOut = extensionElements.getElementsByTagNameNS(this.CAMUNDA_NAMESPACE, 'out')[0];
          if (!camundaOut) {
            camundaOut = doc.createElementNS(this.CAMUNDA_NAMESPACE, 'camunda:out');
            extensionElements.appendChild(camundaOut);
          }
          if (outLocalCheck) {
            camundaOut.setAttribute('local', 'true');
          }
          camundaOut.setAttribute('variables', 'all');
        } else if (extensionElements) {
          // Remove all <camunda:out> elements if outVariablesCheck is false
          const camundaOutElements = extensionElements.getElementsByTagNameNS(this.CAMUNDA_NAMESPACE, 'out');
          for (const camundaOut of Array.from(camundaOutElements)) {
            extensionElements.removeChild(camundaOut);
          }
        }

        if (extensionElements && extensionElements.childNodes.length === 0) {
          element.removeChild(extensionElements);
        }

        element.removeAttribute('camunda:inVariablesCheck');
        element.removeAttribute('camunda:inLocalCheck');
        element.removeAttribute('camunda:outVariablesCheck');
        element.removeAttribute('camunda:outLocalCheck');
      }
    };
    function transformCamundaInputMappingParameters(elementType: string, parameterType: string) {
      const elements = doc.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', elementType);

      for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        const extensionElements = element.getElementsByTagNameNS('http://www.omg.org/spec/BPMN/20100524/MODEL', 'extensionElements')[0];

        if (extensionElements) {
          const inputMappings = extensionElements.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', 'inputMapping');

          for (let j = 0; j < inputMappings.length; j++) {
            const inputMapping = inputMappings[j];
            const inputParameters = inputMapping.getElementsByTagNameNS('http://camunda.org/schema/1.0/bpmn', parameterType);

            const newInputParameters: Element[] = [];

            for (let k = 0; k < inputParameters.length; k++) {
              const inputParameter = inputParameters[k];
              const type = inputParameter.getAttribute('type');
              const source = inputParameter.getAttribute('source');
              const target = inputParameter.getAttribute('target');
              const sourceExpression = inputParameter.getAttribute('sourceExpression');

              if (type !== null) {
                const newInputParameter = doc.createElementNS('http://camunda.org/schema/1.0/bpmn', `camunda:${parameterType}`);

                if (source !== null) {
                  newInputParameter.setAttribute('source', source);
                }
                if (sourceExpression !== null) {
                  newInputParameter.setAttribute('sourceExpression', sourceExpression);
                }
                if (target !== null) {
                  newInputParameter.setAttribute('target', target);
                }

                newInputParameters.push(newInputParameter);
              }
            }


            if (newInputParameters.length > 0) {
              for (const newEl of newInputParameters) {
                extensionElements.insertBefore(newEl, inputMapping);
              }
              extensionElements.removeChild(inputMapping);
            }
          }
        }
      }
    }



    transformSequenceFlows();
    transformExecutionListeners('process');
    transformExecutionListeners('startEvent');
    transformExecutionListeners('userTask');
    transformExecutionListeners('serviceTask');
    transformExecutionListeners('sendTask');
    transformExecutionListeners('receiveTask');
    transformExecutionListeners('exclusiveGateway');
    transformExecutionListeners('inclusiveGateway');
    transformExecutionListeners('complexGateway');
    transformExecutionListeners('eventBasedGateway');
    transformExecutionListeners('parallelGateway');
    transformExecutionListeners('endEvent');
    transformExecutionListeners('subProcess');
    transformExecutionListeners('businessRuleTask');
    transformExecutionListeners('sequenceFlow');

    transformCamundaFields('serviceTask');

    transformCamundaInputParameters('userTask', 'inputParameter');
    transformCamundaInputParameters('receiveTask', 'inputParameter');
    transformCamundaInputParameters('sendTask', 'inputParameter');
    transformCamundaInputParameters('subProcess', 'inputParameter');
    transformCamundaInputParameters('businessRuleTask', 'inputParameter');
   transformCamundaInputMappingParameters('callActivity','in');

    transformCamundaInputParameters('userTask', 'outputParameter');
    transformCamundaInputParameters('receiveTask', 'outputParameter');
    transformCamundaInputParameters('sendTask', 'outputParameter');
    transformCamundaInputParameters('subProcess', 'outputParameter');
    transformCamundaInputParameters('businessRuleTask', 'outputParameter');

    transformBusinessRuleTasks('businessRuleTask');
    transformBusinessRuleTasks('serviceTask');
    transformBusinessRuleTasks('sendTask');
    removeElementsByCamundaType('report');
    removeElementsByCamundaType('createUserPP');
    removeElementsByCamundaType('createUserPM');
    removeElementsByCamundaType('saveDesignation');
    removeElementsByCamundaType('completeProfile');
    removeElementsByCamundaType('registrationWaitingList');
    removeElementsByCamundaType('fixVariablesPPForDMN');
    removeElementsByCamundaType('unsubscribeFromWaitingList');
    removeElementsByCamundaType('renewalRegionWaitingList');
    removeElementsByCamundaType('renewalCategoryWaitingList');
    removeElementsByCamundaType('savePMData');
    removeElementsByCamundaType('saveAttributes');
    removeElementsByCamundaType('createPMLab');
    transformJobExecution('startEvent');
    transformJobExecution('userTask');
    transformJobExecution('serviceTask');
    transformJobExecution('sendTask');
    transformJobExecution('receiveTask');
    transformJobExecution('exclusiveGateway');
    transformJobExecution('inclusiveGateway');
    transformJobExecution('complexGateway');
    transformJobExecution('eventBasedGateway');
    transformJobExecution('parallelGateway');
    transformJobExecution('endEvent');
    transformJobExecution('subProcess');
    transformJobExecution('businessRuleTask');
    addMessageDetails('sendTask');
    transformMultiInstanceLoopCharacteristics();
    addCallActivityDetails('callActivity');
    return serializer.serializeToString(doc);
  }

  private removeCamundaTypeFormAttribute(xml: string): string {
    return xml.replace(/\s+camunda:typeForm="[^"]*"/g, '');
  }

  private updateCamundaTypeAttribute(xml: string): string {
    return xml.replace(/\s+camunda:type="[^"]*"/g, function (match) {
      const value = match.split('=')[1].replace(/"/g, '');
      return ['dmn', 'class', 'expression', 'delegateExpression'].includes(value) ? '' : match;
    });
  }

  protected override bindHotkeys() {


    super.bindHotkeys({
      'ctrl+a, command+a': ModelerActions.selectElements,
      e: ModelerActions.directEditing,
      h: ModelerActions.handTool,
      l: ModelerActions.lassoTool,
      s: ModelerActions.spaceTool,
      'ctrl+=, command+=': ModelerActions.zoomIn,
      'ctrl+-, command+-': ModelerActions.zoomOut,
      'ctrl+0, command+0': ModelerActions.resetZoom,
      'ctrl+9, command+9': ModelerActions.zoomToFit,
      'ctrl+z, command+z': ModelerActions.undo,
      'ctrl+shift+z, command+shift+z': ModelerActions.redo,
      Backspace: ModelerActions.removeSelection,
      'ctrl+c, command+c': ModelerActions.copy,
      c: ModelerActions.globalConnectTool,
      'ctrl+v, command+v': ModelerActions.paste,
      'ctrl+x, command+x': ModelerActions.cut,
      'ctrl+f, command+f': ModelerActions.find
    });
  }

  protected override unbindHotkeys() {

    super.unbindHotkeys();
  }
}
