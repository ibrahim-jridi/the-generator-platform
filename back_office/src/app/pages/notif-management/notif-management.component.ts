import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../shared/models/paginationArgs.model';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { NotificationService } from '../../shared/services/notification.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';

@Component({
  selector: 'app-notif-management',
  templateUrl: './notif-management.component.html',
  styleUrl: './notif-management.component.scss'
})
export class NotifManagementComponent {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected notif: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected notifId: string;
  protected notifName: string;
  protected totalElements: number;
  public currentUserStationId: string;
  private paginationArgs: PaginationArgs;
  public state = window.history.state;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private translateService: TranslateService,
    private notificationService: NotificationService,
    private toastrService: AppToastNotificationService
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadNotification();
    this.initTableAction();
    this.initTableColumns();
    this.translateService.onLangChange.subscribe({
      next: () => {
        this.loadNotification();
      }
    });
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadNotification();
  }

  private formatDate(dateString: string): string {
    if (!dateString) {
      return '';
    }
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }

  private loadNotification(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.page,
      size: this.pageSize
    };
    this.notificationService.getNotification().subscribe((data) => {
      this.notif = data
        .map((item) => ({
          ...item,
          displayDate: this.formatDate(item.createdDate),
          originalDate: new Date(item.createdDate),
          frequencyEnum: this.translatePipe.transform(`notif.${item.frequencyEnum}`),
          broadcastChannel: this.translatePipe.transform(`notif.${item.broadcastChannel}`),
          destinationType: item.destinationType ? this.translatePipe.transform(`notif.destination_type.${item.destinationType}`) : this.translatePipe.transform('notif.N/A')
        }))
        .sort((a, b) => b.originalDate.getTime() - a.originalDate.getTime());
    });
  }

  private handleOnViewForm(id: string, name: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        notifId: id,
        userName: name
      }
    };
    this.router.navigate(['/pages/notif-management/notif-view', name], navigationExtras);
  }

  private handleOnUpdateNotif(id: string, name: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        notifId: id
      }
    };
    this.router.navigate(['/pages/notif-management/notif-update', name], navigationExtras);
  }

  protected handleOnDeleteNotif(): void {
    this.notificationService.deleteNotification(this.notifId).subscribe({
      next: (data) => {
        this.toastrService.onSuccess(this.translatePipe.transform('notif.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
        this.deleteUserModal.hide();
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('notif.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private updateTable(): void {
    this.notif = [];
    this.loadNotification();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewForm(event.objectClicked.id, event.objectClicked.name);
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateNotif(event.objectClicked.id, event.objectClicked.name);
    }
    if (event.buttonActionId === 'delete') {
      this.deleteUserModal.show();
      this.notifId = event.objectClicked.id;
      this.notifName = event.objectClicked.name;
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('notif.NOM', 'name', false));
    this.tableColumns.push(new CustomTableColonneModel('notif.CREATED_DATE', 'displayDate', false));
    this.tableColumns.push(new CustomTableColonneModel('notif.FREQUENCE', 'frequencyEnum', false));
    this.tableColumns.push(new CustomTableColonneModel('notif.TYPE', 'broadcastChannel', false));
    this.tableColumns.push(new CustomTableColonneModel('notif.DESTINATAIRE', 'destinationType', false));
  }

  protected redirectToAddNotif(): void {
    this.router.navigate(['/pages/notif-management/notif-add']);
  }
}
