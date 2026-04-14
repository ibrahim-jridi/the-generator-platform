import {Component, OnInit} from '@angular/core';
import {TranslatePipe} from "@ngx-translate/core";
import {ActivityAuditModel} from "../../../../shared/models/activity-audit.model";
import {OperationType} from "../../../../shared/enums/operation_type.enum";
import {ActivityAuditService} from "../../../../shared/services/activity-audit.service";
import {
  AppToastNotificationService
} from "../../../../shared/services/appToastNotification.service";
import {TokenUtilsService} from "../../../../shared/services/token-utils.service";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-view-activity-audit',
  templateUrl: './view-activity-audit.component.html',
  styleUrls: ['./view-activity-audit.component.scss']
})
export class ViewActivityAuditComponent implements OnInit {

  protected activityAuditId: string;
  protected activityAudit: ActivityAuditModel;
  public state = window.history.state;
  protected readonly OperationType = OperationType;

  constructor(
    private activityAuditService: ActivityAuditService,
    private toastService: AppToastNotificationService,
    private translatePipe: TranslatePipe, public utilService: TokenUtilsService) {
  }

  public ngOnInit(): void {
    this.activityAuditId = this.state.auditId
    this.handleOnGetActivityAudit(this.activityAuditId);
  }

  public handleOnGetActivityAudit(id: string): void {
    this.activityAuditService.getActivityAuditById(id).subscribe({
      next: ((data: ActivityAuditModel): void => {
        if (data != undefined) {
          this.activityAudit = data;
        }
      })
      , error: () => {
        this.toastService.onError('', this.translatePipe.transform('menu.ERROR'));
      }
    })
  }

  protected translateAuditDetails(details: string): string {
    const oldTranslated = this.translatePipe.transform("audit.OLD");
    const newTranslated = this.translatePipe.transform("audit.NEW");
    const attributeTranslated = this.translatePipe.transform("audit.ATTRIBUTE");

    const replaceOlds = details.replace(/Old/gi, oldTranslated);

    const replaceNews = replaceOlds.replace(/New/gi, newTranslated);

    const replaceAttributes = replaceNews.replace(/Attribute/gi, attributeTranslated);

    return replaceAttributes;
  }

  public getTranslatedAuditDetails(): string {
    if (this.activityAudit?.details) {
      return this.translateAuditDetails(this.activityAudit.details);
    }
    return '';
  }

  protected translateEntity(entity: string | undefined): string {
    return this.translatePipe.transform(`entity.${entity}`);
  }

  protected translateAction(action: string): string {
    return this.translatePipe.transform(`audit.${action}`);
  }

  public formattedDate(date: Date): string {
    if(date == null) {
      return 'N/A'
    }
    return formatDate(date, 'dd/MM/yyyy HH:mm:ss', 'en-US');
  }
}
