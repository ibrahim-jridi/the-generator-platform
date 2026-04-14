import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { CamundaService } from 'src/app/shared/services/camunda.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { WaitingListService } from 'src/app/shared/services/waiting-list.service';
import { NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-renewal-region-waiting-list',
  templateUrl: './renewal-region-waiting-list.component.html',
  styleUrl: './renewal-region-waiting-list.component.scss'
})
export class RenewalRegionWaitingListComponent {

    private processDefKey: string = 'RENEWAL-REGION-WAITING-LIST';
    protected userId: string;

    constructor(
      private tokenUtilisService: TokenUtilsService,
      private camundaService: CamundaService,
      private waitingListService:WaitingListService,
      private toastrService: AppToastNotificationService,
      private translatePipe: TranslatePipe,
      private router: Router,
    ){

    }

     public ngOnInit(): void {

       this.userId = this.tokenUtilisService.getUserId();

       this.waitingListService.getWaitingListByUserId(this.userId).subscribe({
        next: (data: any) => {
        let categoryLabel: string;
        if (data.category === "CATEGORY_A") {
          categoryLabel = "categorieAPharmacieDeJour";}
        else if (data.category === "CATEGORY_B") {
          categoryLabel = "categorieBPharmacieDeNuit";}
        const vars = {
          starter:this.userId,
          LIST_CATEGORY_CHANGE_RANK_NUMBER: data.rank,
          LIST_SUBMIT_FORM_PHARMACY_CATEGORY: categoryLabel,
          LIST_SUBMIT_FORM_GOVERNORATE_LIST:data.governorate,
        };
          for (let i = 1; i <= 24; i++) {
                     vars[`LIST_SUBMIT_FORM_DELEGATION${i}`] = data.delegation;
          }
          for (let i = 1; i <= 24; i++) {
                     vars[`LIST_SUBMIT_FORM_COMMUNES${i}`] = data.municipality;
          }
       this.camundaService.startProcessbyVariable(this.processDefKey, vars).subscribe((data) => {
         this.camundaService.getTaskByProcessInstanceId(data.processInstanceId).subscribe(
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

       this.toastrService.onInfo(this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'), this.translatePipe.transform('menu.INFO'));
          });

     }

   });
  }

}








