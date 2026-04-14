import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { TransactionService } from 'src/app/shared/services/transaction.service';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.scss'
})
export class TransactionListComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected transaction: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected transactionId: string;
  protected totalElements: number;
  protected currentUserStationId: string;
  private paginationArgs: PaginationArgs;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private transactionService: TransactionService,
    private toastrService: AppToastNotificationService
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadTransaction();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadTransaction();
  }

  private loadTransaction(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };

    this.transactionService.getTransactions().subscribe({
      next: (response: any) => {
        if (response) {
          this.transaction = response || [];
        }
      },
      error: (error) => {
        this.toastrService.onError(
          this.translatePipe.transform('transaction.FAILED_TO_LOAD_TRANSACTION'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }

  handleOnViewTransaction(id: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        transactionId: id // Database id
      }
    };
    this.router.navigate(['view-transaction', id], navigationExtras);
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewTransaction(event.objectClicked.id);
      //this.router.navigate(['pages/user-management/internal-user-management/view'])
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('transaction.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('transaction.CONDIDAT_FIRSTNAME', 'condidat_firstname', false));
    this.tableColumns.push(new CustomTableColonneModel('transaction.CONDIDAT_LASTNAME', 'condidat_lastname', false));
    this.tableColumns.push(new CustomTableColonneModel('transaction.PROCESS_NAME', 'process_name', false));
    this.tableColumns.push(new CustomTableColonneModel('transaction.AMOUNT', 'amount', false));
    this.tableColumns.push(new CustomTableColonneModel('transaction.STATUS', 'status', false));
  }
}
