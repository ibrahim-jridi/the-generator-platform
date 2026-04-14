import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HistoricTaskInstance} from '../../../shared/models/historicTaskInstance.model';
import {FormioComponent} from '@formio/angular';
import {Form} from '../../../shared/models/form.model';
import {CamundaService} from '../../../shared/services/camunda.service';

@Component({
  selector: 'app-process-history',

  templateUrl: './process-history.component.html',
  styleUrl: './process-history.component.scss'
})
export class ProcessHistoryComponent implements OnInit {
  @ViewChild(FormioComponent) formioComponent: FormioComponent;

  protected taskId: string;

  isCollapsed: boolean;
  tasks: any[] = [];
  historicTasks: HistoricTaskInstance[] = [];
  private processInstanceId: string;
  public state = window.history.state;

  constructor(private camundaService: CamundaService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    
    this.processInstanceId = this.state.processInstanceId;
    this.handleOnGetProcessHistory(this.processInstanceId);
  }

  public handleOnGetProcessHistory(processInstanceId: string): void {
    this.camundaService.getHistoricTasks(processInstanceId).subscribe({
      next: (task: any) => {
        this.historicTasks = task;
        this.historicTasks.forEach((task) => {
          if (task.form && task.form.fields && task.id !== this.taskId) {
            const formFields = JSON.parse(task.form.fields);
            const historicForm: Form = {
              display: formFields.display || 'form',
              settings: formFields.settings || {},
              components: formFields.components || []
            };
            const submissionData = {};
            if (task.submissionDTO && task.submissionDTO.length > 0) {
              task.submissionDTO.forEach((submission) => {
                submissionData[submission.key] = JSON.parse(submission.value);
              });
            }
            this.tasks.push({
              id: task.id,
              isCollapsed: true,
              name: task.name || '',
              assignee: `${task.userDTO.firstName || ''} ${task.userDTO?.lastName || ''} (${task.userDTO?.username || ''})`,
              startTime: task.startTime || '',
              endTime: task.endTime || '',
              disabled: false,
              form: historicForm,
              submission: submissionData
            });
          }
        });
      },
      error: (error) => {
        console.error('Error fetching tasks:', error);
      }
    });
  }
}
