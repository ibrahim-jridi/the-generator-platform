import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HistoricTaskInstance} from '../../../shared/models/historicTaskInstance.model';
import {Form} from '../../../shared/models/form.model';
import {CamundaService} from '../../../shared/services/camunda.service';
import {TokenUtilsService} from "../../../shared/services/token-utils.service";

@Component({
  selector: 'app-view-historic-task',
  templateUrl: './view-historic-task.component.html',
  styleUrls: ['./view-historic-task.component.scss']
})
export class ViewHistoricTaskComponent implements OnInit {
  protected taskId: string;
  isCollapsed: boolean;
  tasks: any[] = [];
  historicTasks: HistoricTaskInstance[] = [];
  private processInstanceId: string;
  public state = window.history.state;
  constructor(private camundaService: CamundaService, private route: ActivatedRoute, private tokenUtilsService: TokenUtilsService) { }

  ngOnInit(): void {
    this.processInstanceId = this.state.processInstanceId;
    this.handleOnGetViewHistoric(this.processInstanceId);
    }
  public handleOnGetViewHistoric(processInstanceId: string): void {
    this.camundaService.getHistoricTasks(processInstanceId).subscribe({
      next: (task: any) => {
        this.historicTasks = task;
        const currentUserId = this.tokenUtilsService.getUserId();
        const isPrivileged = this.tokenUtilsService.isInt() || this.tokenUtilsService.isAdministrator() || this.tokenUtilsService.isPM();

        const starterUserId = task[0]?.assignee || task[0]?.userDTO?.id;
        const isStarter = starterUserId === currentUserId;
        this.historicTasks.forEach(task => {
          if (task.form && task.form.fields && task.id !== this.taskId) {
            let canViewTask = false;

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
            const formFields = JSON.parse(task.form.fields);
            const historicForm: Form = {
              display: formFields.display || 'form',
              settings: formFields.settings || {},
              components: formFields.components || []
            };
            const submissionData = {};
            if (task.submissionDTO && task.submissionDTO.length > 0) {
              task.submissionDTO.forEach(submission => {
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
              submission:submissionData
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
