import {Component, OnInit} from '@angular/core';
import {AppToastNotificationService} from "../../../../shared/services/appToastNotification.service";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {formatDate} from '@angular/common';
import {UserService} from "../../../../shared/services/user.service";
import {User} from "../../../../shared/models/user.model";
import {ErrorAuditModel} from "../../../../shared/models/error-audit.model";
import {ErrorAuditService} from "../../../../shared/services/error-audit.service";

@Component({
  selector: 'app-view-error-audit',
  standalone: true,
  imports: [
    TranslateModule
  ],
  templateUrl: './view-error-audit.component.html',
  styleUrl: './view-error-audit.component.scss'
})
export class ViewErrorAuditComponent implements OnInit {

  protected errorAuditId: string;
  protected errorAudit: ErrorAuditModel;
  public state = window.history.state;
  protected user: User;

  constructor(
    private errorAuditService: ErrorAuditService,
    private toastrService: AppToastNotificationService,
    private userService: UserService,
    private translatePipe: TranslatePipe) {
  }

  public ngOnInit(): void {
    this.errorAuditId = this.state.auditId
    this.handleOnGetErrorAudit(this.errorAuditId);

  }

  public handleOnGetErrorAudit(id: string): void {
    this.errorAuditService.getErrorAuditById(id).subscribe({
      next: ((data: ErrorAuditModel): void => {
        if (data != undefined) {
          this.errorAudit = data;
          this.handleOnGetUserById(this.errorAudit.createdBy);
        }
      })
      , error: () => {
        this.toastrService.onError('', this.translatePipe.transform('menu.ERROR'));
      }
    })
  }


  protected translateType(errorType: string | undefined): string {
    return this.translatePipe.transform(`audit.error_type.${errorType}`);
  }

  protected formatDate(date: any): string {
    return formatDate(date, 'dd/MM/yyyy HH:mm:ss', 'en-US')
  }

  public handleOnGetUserById(id: string): void {
    this.userService.getUserById(id).subscribe({
      next: (data: User): void => {
        if (data != undefined) {
          this.user = data;
        }
      }
      , error: () => {
        this.toastrService.onError('', this.translatePipe.transform('menu.ERROR'));
      }
    })
  }


  protected translateStatus(errorStatus: string): string {
    return this.translatePipe.transform(`audit.error_status.${errorStatus}`);
  }

}
