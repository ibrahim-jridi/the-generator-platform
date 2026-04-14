import { Component, OnInit } from '@angular/core';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { NavigationExtras, Router } from '@angular/router';
import { CamundaService } from '../../shared/services/camunda.service';

@Component({
  selector: 'app-industrie-vet',
  templateUrl: './industrie-vet.component.html',
  styleUrl: './industrie-vet.component.scss'
})
export class IndustrieVetComponent implements OnInit {
  constructor(
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
  ) {}
  ngOnInit() {
    let profileCompleted = sessionStorage.getItem('profileCompleted');

    const processKey: string = 'INDUSTRIE_VETERINAIRE_MODEL_LTS';
    if (profileCompleted == "true") {
      this.startProcessByKeyAndRedirectToValidateTask(processKey);
    } else {
      this.toastrService.onError(this.translatePipe.transform('task.SHOULD_COMPLETE_PROFILE'), this.translatePipe.transform('menu.ERROR'));
      this.router.navigate(['/pages/dashboard']);
    }
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
