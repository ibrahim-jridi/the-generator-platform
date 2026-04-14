import {Component, OnInit} from '@angular/core';
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";
import {TranslatePipe} from "@ngx-translate/core";
import {NavigationExtras, Router} from "@angular/router";
import {CamundaService} from "../../shared/services/camunda.service";
import {WaitingListService} from "../../shared/services/waiting-list.service";
import {TokenUtilsService} from "../../shared/services/token-utils.service";

@Component({
  selector: 'app-renewal-category-waiting-list',
  templateUrl: './renewal-category-waiting-list.component.html',
  styleUrl: './renewal-category-waiting-list.component.scss'
})
export class RenewalCategoryWaitingListComponent  implements OnInit{
  private processDefKey: string = 'RENEWAL-CATEGORY-WAITING-LIST';
  protected userId: string;
  constructor(
      private toastrService: AppToastNotificationService,
      private translatePipe: TranslatePipe,
      private router: Router,
      private camundaService: CamundaService,
      private waitingListService: WaitingListService,
      private tokenUtilisService: TokenUtilsService,
  ) {
  }

  ngOnInit() {

    this.userId = this.tokenUtilisService.getUserId();
    this.waitingListService.getWaitingListByUserId(this.userId).subscribe({
      next: (data: any) => {

        let categoryLabel: string;

        if (data.category === "CATEGORY_A") {
          categoryLabel = "categorieAPharmacieDeJour";
        } else if (data.category === "CATEGORY_B") {
          categoryLabel = "categorieBPharmacieDeNuit";
        }
        const vars = {
          starter: this.userId,
          LIST_SUBMIT_FORM_COMMUNES: data.municipality,
          LIST_SUBMIT_FORM_DELEGATION: data.delegation,
          LIST_SUBMIT_FORM_GOVERNORATE_LIST: data.governorate,
          INSCRIPTION_WAITING_LIST_SUBMIT_FORM_PHARMACY_CATEGORY: categoryLabel,
          LIST_CATEGORY_CHANGE_RANK_NUMBER:data.rank
        };
            this.camundaService.startUniqueProcessInstanceByKey(this.processDefKey, vars).subscribe(
              (res ) => {

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
                this.toastrService.onInfo(this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'), this.translatePipe.transform('menu.INFO'));
              },
                (error) => {
                  this.toastrService.onError(this.translatePipe.transform('task.INVALID_ACTIVE_TASK_CATEGORY'), this.translatePipe.transform('menu.ERROR'));
                }
                );

      }
    })
  }
  }



