import { Component, OnInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { CamundaService } from '../../shared/services/camunda.service';

@Component({
  selector: 'app-declaration-interests-management',
  templateUrl: './digital-engagement-management.component.html',
  styleUrls: ['./digital-engagement-management.component.scss']
})
export class DigitalEngagementManagementComponent implements OnInit {
  constructor(
    private tokenUtilisService: TokenUtilsService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
    private tokenUtilsService: TokenUtilsService
  ) {}

  ngOnInit() {
    const processKey: string = 'DIGITAL_ENGAGEMENT';
    const userQualified = this.tokenUtilisService.isPM();
    if (userQualified) {
      this.startProcessByKeyAndRedirectToValidateTask(processKey);
    } else if (!this.tokenUtilisService.isPM()) {
      this.toastrService.onError(this.translatePipe.transform('individual.NOT_ALLOWED_PP'), this.translatePipe.transform('menu.ERROR'));
      this.router.navigate(['/pages/dashboard']);
    } else {
      this.toastrService.onError(this.translatePipe.transform('task.SHOULD_COMPLETE_PROFILE'), this.translatePipe.transform('menu.ERROR'));
      this.router.navigate(['/pages/dashboard']);
    }
  }

  startProcessByKeyAndRedirectToValidateTask(processKey: string) {
    const userId = this.tokenUtilsService.getUserId();
    const variables = {
      starter: userId
    };
    this.camundaService.startProcessOnceByKeyAndGetStartedInstance(processKey, variables).subscribe(
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
            const navigationExtras: NavigationExtras = {
              state: {
                processInstanceId: res.processInstanceId
              }
            };
            this.router.navigate(['pages/instance-management/view-history'], navigationExtras);
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
