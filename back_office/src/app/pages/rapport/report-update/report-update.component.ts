import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { Report} from '../../../shared/models/report.model';

@Component({
  selector: 'app-report-update',
  templateUrl: './report-update.component.html',
  styleUrl: './report-update.component.scss'
})
export class ReportUpdateComponent {

  protected report : any;
  protected reportForm: any;
  public state = window.history.state;
  private reportId: string;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private toastrService: AppToastNotificationService
  ) {

  }

  public ngOnInit(): void {
    this.reportId = this.state.reportId;
    this.initReportForm();
    this.loadReport();
  }


  private loadReport(): void {
    this.report = {
      nom: 'report',
      description: 'report',
      file: 'report'


    };
  }

  private initReportForm(): void {
    this.reportForm = this.fb.group({
      name: [
        'report',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      description: [
        'report',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      file: [
        { value: 'report', disabled: true },

        [
          Validators.required
        ],
      ]

    });

  }



  public get name() {
    return this.reportForm?.get('name')!;
  }

  public get description() {
    return this.reportForm?.get('description')!;
  }

  public get file() {
    return this.reportForm?.get('file')!;
  }


  private fillReportData(): void {
    this.report.nom=this.reportForm.value.name;
    this.report.description = this.reportForm.value.description;
    this.report.file = this.reportForm.value.file;

  }

  protected updateReport(): void {
    this.fillReportData();
  }



  protected navigateToUsers(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  public viewToasterError(): void {
    this.toastrService.onError(this.translatePipe.transform('report.errors.ADD_ERROR'), this.translatePipe.transform('menu.ERROR'));
  }

  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('report.ADD_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
  }

  protected back(): void {
    this.router.navigate(['/pages/report-management']);
  }

}
