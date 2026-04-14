import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { TransactionService } from 'src/app/shared/services/transaction.service';

@Component({
  selector: 'app-transaction-view',
  templateUrl: './transaction-view.component.html',
  styleUrls: ['./transaction-view.component.scss']
})
export class TransactionViewComponent implements OnInit {

  protected transaction: any;
  protected state: any;
  protected transactionId: string;
  protected transactionForm: FormGroup;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private toastrService: AppToastNotificationService
  ) { }

  public ngOnInit(): void {
    this.state = window.history.state;
    this.transactionId = this.state.navigationId;
    this.handleOnGetGroup(this.transactionId);
  }

  private initFormTransaction(transaction: any): void {
    this.transactionForm = this.fb.group({
      condidat_firstname: [{ value: transaction.condidat_firstname, disabled: true }, [Validators.required]],
      condidat_lastname: [{ value: transaction.condidat_lastname, disabled: true }, [Validators.required]],
      process_name: [{ value: transaction.process_name, disabled: true }, [Validators.required]],
      amount: [{ value: transaction.amount, disabled: true }, [Validators.required]],
      status: [{ value: transaction.status, disabled: true }, [Validators.required]]
    });
  }

  public handleOnGetGroup(id: string): void {
    this.transactionService.getTransactionById(id).subscribe({
      next: (data: any) => {
        this.transaction = data;
        this.initFormTransaction(this.transaction);
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('transaction.FAILED_TO_LOAD_TRANSACTION'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }
}
