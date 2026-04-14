import { Component, OnInit } from '@angular/core';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { NavigationExtras, Router } from '@angular/router';
import { CamundaService } from '../../shared/services/camunda.service';

@Component({
  selector: 'app-create-grossiste',
  templateUrl: './create-grossiste.component.html',
  styleUrl: './create-grossiste.component.scss'
})
export class CreateGrossisteComponent implements OnInit{

  constructor(
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
  ) {}

  ngOnInit() {
    const processKey: string = 'CREATION_GROSSISTE_MODEL_LTS';
    this.startProcessByKeyAndRedirectToValidateTask(processKey);
  }

  startProcessByKeyAndRedirectToValidateTask(processKey: string) {

    this.camundaService.startProcess(processKey).subscribe(
      (res) => {
        this.camundaService.getTaskByProcessInstanceId(res.processInstanceId).subscribe(
          (data) => {
            const navigationExtras: NavigationExtras = {
              state: {
                processInstanceId: data.executionId,
                taskId: data.id
              }
            };
            this.router.navigate(['pages/task-management/task-list/validate-task', data.name], navigationExtras);
          },
          (error) => {
            this.toastrService.onError(this.translatePipe.transform('task.FAILED_TO_GET_TASK'), this.translatePipe.transform('menu.ERROR'));
          }
        );
        console.log(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
