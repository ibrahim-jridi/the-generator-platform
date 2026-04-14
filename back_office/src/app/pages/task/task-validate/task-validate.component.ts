import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {HistoricTaskInstance} from '../../../shared/models/historicTaskInstance.model';
import {AppToastNotificationService} from '../../../shared/services/appToastNotification.service';
import {FormioComponent} from '@formio/angular';
import {Form} from '../../../shared/models/form.model';
import {forkJoin} from 'rxjs';
import {CamundaService} from '../../../shared/services/camunda.service';
import {FormService} from '../../../shared/services/form.service';
import {Task} from '../../../shared/models/task.model';
import {CommonService} from '../../../shared/services/common.service';
import {WaitingListService} from '../../../shared/services/waiting-list.service';
import {TokenUtilsService} from '../../../shared/services/token-utils.service';
import {LoaderService} from "../../../shared/services/loader.service";

@Component({
  selector: 'app-task-validate',
  templateUrl: './task-validate.component.html',
  styleUrls: ['./task-validate.component.scss']
})
export class TaskValidateComponent implements OnInit {
  @ViewChild(FormioComponent) formioComponent: FormioComponent;
  public taskId: string;
  public historicTasks: HistoricTaskInstance[] = [];
  public processInstanceId: string;
  public tasks: Task[] = [];
  public historicViewTask: Task[] = [];
  public currentTasks: Task[] = [];
  public assigneeId: string;
  public variables1: any;
  public form: Form = {};
  public task: Task[] = [];
  public submissionData: Map<String, any>;
  public state = window.history.state;

  constructor(
    private readonly camundaService: CamundaService,
    private readonly translatePipe: TranslatePipe,
    private readonly toasterService: AppToastNotificationService,
    private readonly formService: FormService,
    private readonly router: Router,
    private commonService: CommonService,
    private waitingListService: WaitingListService,
    private tokenUtilsService: TokenUtilsService,
    private loaderService: LoaderService
  ) {
  }

  public ngOnInit(): void {
    this.taskId = this.state.taskId;
    const processInstanceId = this.state.processInstanceId;
    this.handleOnGetTask(processInstanceId);
  }

  public handleOnGetTask(processInstanceId: string): void {
    forkJoin({
      currentTask: this.camundaService.getTaskById(this.taskId),
      historicTasks: this.camundaService.getHistoricTasks(processInstanceId),
      variables: this.camundaService.getProcessVariablesByProcessInstanceId(processInstanceId),
      // drafts: this.formService.getDraftById(this.taskId)
    }).subscribe({
      next: ({ currentTask, historicTasks, variables }) => {
        this.variables1 = variables;
        this.processCurrentTask(currentTask, variables, null);
        this.processHistoricTasks(historicTasks);

        // Merge tasks and notify success
        this.tasks = [...this.historicViewTask, ...this.currentTasks];
        this.toasterService.onSuccess(this.translatePipe.transform('task.TASKS_LOADED_SUCCESSFULLY'), this.translatePipe.transform('task.SUCCESS'));
      },
      error: (err) => {
        this.toasterService.onError(this.translatePipe.transform('task.FAILED_TO_LOAD_TASKS'), this.translatePipe.transform('task.ERROR'));
      }
    });
  }

  private processCurrentTask(currentTask: any, variables: any, draft: any): void {
    if (!currentTask) return;

    this.taskId = currentTask.id;
    this.assigneeId = currentTask.assignee;

    if (currentTask?.form?.fields) {
      const dynamicForm = this.parseFormFields(currentTask.form.fields, currentTask.form.id);

      this.formService.getSubmissionsByTaskInstanceId(currentTask.id).subscribe({
        next: (submissions) => {
          let submissionData = {};
          // Check if draft exists before using it
          if (draft != null && draft.submission != null) {
            submissionData = JSON.parse(draft.submission);
          }

          this.currentTasks.push({
            id: currentTask.id,
            isCollapsed: false,
            name: currentTask.name || '',
            assignee: `${currentTask.userDTO?.firstName || ''} ${currentTask.userDTO?.lastName || ''} (${currentTask.userDTO?.username || ''})`,
            assigneeUserName: currentTask.userDTO?.username || '',
            startTime: currentTask.createTime || '',
            disabled: true,
            form: dynamicForm,
            submission: submissionData,
            variables: variables.body,
            businessKey: currentTask.businessKey || ''
          });

          this.tasks = [...this.historicViewTask, ...this.currentTasks];
        },
        error: (err) => {
          console.error('Error fetching submissions:', err);
          this.toasterService.onError('Failed to load task submissions', 'Error');
        }
      });
    }
  }
  private processHistoricTasks(historicTasks: any[]): void {
    if (!historicTasks || historicTasks.length === 0) {
      return;
    }
    const currentUserId = this.assigneeId;
    let canViewTask = false;
    const isPrivileged = this.tokenUtilsService.isInt() || this.tokenUtilsService.isAdministrator() || this.tokenUtilsService.isPM();
    const starterUserId = historicTasks[0]?.assignee || historicTasks[0]?.userDTO?.id;
    const isStarter = starterUserId === currentUserId;
    historicTasks.forEach(task => {

      if (isStarter ||isPrivileged) {
        canViewTask = true;
      }
      else {
        canViewTask =
            task.assignee === currentUserId ||
            task.userDTO?.id === currentUserId;
      }
      if (!canViewTask) {
        return;
      }

      if (task?.form?.fields && task.id !== this.taskId) {
        const historicForm = this.parseFormFields(task.form.fields);
        let submissionData: object = {};
        if (task.status == 'COMPLETED' && task.submissionDTO && task.submissionDTO.length > 0) {
          task.submissionDTO.forEach((submission) => {
            submissionData[submission.key] = JSON.parse(submission.value);
          });
        } else {
          submissionData = JSON.parse(task.submissionDTO);
        }

        this.historicViewTask.push({
          id: task.id,
          isCollapsed: true,
          name: task.name || '',
          assignee: `${task.userDTO?.firstName || ''} ${task.userDTO?.lastName || ''} (${task.userDTO?.username || ''})`,
          startTime: task.startTime || '',
          endTime: task.endTime || '',
          disabled: false,
          form: historicForm,
          submission: submissionData
        });
      }
    });
  }

  private parseFormFields(fields: string, formId: string = ''): any {
    const formFields = JSON.parse(fields);
    return {
      display: formFields.display || 'form',
      settings: formFields.settings || {},
      components: formFields.components || [],
      formId: formId
    };
  }
  public handleFormSubmission(submissionData: any, event: any): void {
    const fileComponents: any[] = [];
    const listKeys: string[] = [];
    const variables = { ...submissionData };
    const components = this.form.components[0]?.components || [];
    this.collectFileComponents(submissionData, fileComponents, listKeys);
    components.forEach((comp) => this.collectFileComponents(comp, fileComponents, listKeys));
    if (fileComponents.length > 0) {
      this.uploadFiles(fileComponents, listKeys, submissionData, variables);
    } else {
      this.validateTask(this.taskId, variables);
    }
  }

  private collectFileComponents(data: any, fileComponents: any[], listKeys: string[]): void {
    if (!data || typeof data !== 'object') return;

    Object.keys(data).forEach((key) => {
      const value = data[key];

      if (key.startsWith('file')) {
        const files = Array.isArray(value) ? value : [value];
        files.forEach((file) => {
          if (file?.originalName) {
            fileComponents.push({ key, file });
            listKeys.push(key);
          }
        });
      } else if (typeof value === 'object') {
        this.collectFileComponents(value, fileComponents, listKeys);
      }
    });
  }

  private uploadFiles(fileComponents: any[], listKeys: string[], submissionData: any, variables: any): void {
    let completedUploads = 0;
    const uploadPath = `${this.currentTasks[0].assigneeUserName}/request/${this.currentTasks[0].name}`;

    fileComponents.forEach(({ key, file }) => {
      if (!file) {
        this.toasterService.onWarning(`No valid file found for ${key}`, 'Error');
        return;
      }
      const selectedFile = file.fileData || file;
      const fileToUpload = new File([selectedFile], selectedFile.originalName, { type: selectedFile.type });

      this.formService.uploadFile(fileToUpload, uploadPath).subscribe({
        next: (response) => {
          if (response.fileMinioId) {
            submissionData[`${key}Uuid`] = response.fileMinioId;
          }
          if (++completedUploads === listKeys.length) {
            this.validateTask(this.taskId, variables);
          }
        },
        error: (error) => {
          if (error.error) this.toasterService.onError('Server response:', error.error);
        }
      });
    });
  }

  public validateTask(taskId: string, variables: any): void {
    this.loaderService.show();
    this.camundaService.validateTask(taskId, variables).subscribe({
      next: (response: any) => {
        this.loaderService.hide();
        this.commonService.validateTaskEmit();
        if (!this.tokenUtilsService.isPM()) {
          this.waitingListService.refreshUserStatus();
        }
        this.toasterService.onSuccess(
          this.translatePipe.transform('task.SUCCESS_TO_VALIDATE_TASK'),
          this.translatePipe.transform('task.SUCCESS_TO_VALIDATE_TASK')
        );
        this.router.navigate(['pages/task-management/task-list']);
      },
      error: (err: any) => {
        this.loaderService.hide();
        this.toasterService.onError(
          this.translatePipe.transform('task.FAILED_TO_VALIDATE_TASK'),
          this.translatePipe.transform('task.FAILED_TO_VALIDATE_TASK')
        );
      }
    });
  }

  public receiveData(data: any): void {
    this.form = data;
  }
}
