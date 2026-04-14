import {Component, OnInit, ViewChild} from '@angular/core';
import {CustomButtonModule, CustomSelectModule, ModalModule} from "../../../../theme/shared/components";
import {ListCostServicesComponent} from "../list-cost-services/list-cost-services.component";
import {NgForOf, NgIf} from "@angular/common";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";
import {CostService} from "../../../../shared/models/cost-service";
import {Subscription} from "rxjs";
import {BsConfigService} from "../../../../shared/services/bs-config.service";
import {AppToastNotificationService} from "../../../../shared/services/appToastNotification.service";
import {CamundaService} from "../../../../shared/services/camunda.service";
import {FrequenceType} from "../../../../shared/services/frequence-type";
import {RenewalType} from "../../../../shared/services/renewal-type";
import {RenewalAmountChoice} from "../../../../shared/services/renewal-amount-choice";
import {CostType} from "../../../../shared/services/cost-type";
import {ActivatedRoute, Router} from "@angular/router";
import {RegexConstants} from "../../../../shared/utils/regex-constants";
import {Constants} from "../../../../shared/utils/constants";
import {NgbCollapse} from "@ng-bootstrap/ng-bootstrap";
import {User} from "../../../../shared/models/user.model";
import {BsConfigurationComponent} from "../../bs-configuration.component";

@Component({
  selector: 'app-detail-cost-service',
  standalone: true,
  imports: [
    CustomButtonModule,
    CustomSelectModule,
    ListCostServicesComponent,
    NgForOf,
    NgIf,
    NgxIntlTelInputModule,
    ReactiveFormsModule,
    TranslateModule,
    NgbCollapse,
    ModalModule
  ],
  templateUrl: './detail-cost-service.component.html',
  styleUrl: './detail-cost-service.component.scss'
})
export class DetailCostServiceComponent implements OnInit{
  public costServiceId: string;
  public serviceConfigForm: FormGroup;
  public coastForm: FormGroup;
  public systemDate: Date;
  public serviceNameList: { id: string; label: any }[] = [];
  public costServiceTypeList: { id: string; label: any }[] = [];
  public frequenceTypeList: { id: string; label: any }[] = [];
  public renewalTypeList: { id: string; label: any }[] = [];
  public renewalAmountChoiceList: { id: string; label: any }[] = [];
  public fixe: boolean = true;
  public creationEntreprise = Constants.CREATION_ENTREPRISE;
  public costService = new CostService();
  private subscriptions: Subscription = new Subscription();
  public isRenewable: boolean = false;
  public keepAmount: boolean = true;
  protected prixHt: number = 0;
  protected tva: number = 0;
  protected renewel_prixHt: number = 0;
  protected renewel_tva: number = 0;
  protected readonly Constants = Constants;
  protected viewMode = true ;
  public selectedCostType :{ id: string; label: any };
  public selectedRenewel :{ id: string; label: any };
  public selectedRenewelChoice :{ id: string; label: any };
  public selectedRenewelFrequenceType :{ id: string; label: any };
  public selectedFrequenceType :{ id: string; label: any };
  public selectedServiceName :{ id: string; label: any };
  public state = window.history.state;
  public minDateEnd: string | null = null;
  public showCostType: boolean = false;
  public showRenewalSection: boolean = false;
  constructor(
    private fb: FormBuilder,
    private configCostService: BsConfigService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private camundaService: CamundaService,
    private frequenceTypeTranslation: FrequenceType,
    private renewalTypeTranslation: RenewalType,
    private renewalAmountChoice: RenewalAmountChoice,
    private costTypeTranslation: CostType,
    private router: Router,
    private route: ActivatedRoute,
    private bsConfig: BsConfigurationComponent
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.initAllLists();

    this.route.paramMap.subscribe((params) => {
      this.viewMode = true;
      this.costServiceId = params.get('id');
      this.systemDate = new Date();
      this.getProcessNameList(() => {
        this.getCostService(this.costServiceId);
      });
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private initForms(): void {
    this.initServiceConfigForm();
    this.initCoastForm();
  }

  private initServiceConfigForm(): void {
    this.serviceConfigForm = this.fb.group({
      serviceName: [this.selectedServiceName, Validators.required],
      coastType: [this.selectedCostType, Validators.required],
    });
    this.disableForm(this.serviceConfigForm, this.viewMode);
  }

  private initCoastForm(): void {
    this.coastForm = this.fb.group({
      prixHt: [this.costService?.prix_ht, Validators.required],
      prixTtc: [{ value: this.costService?.prix_ttc, disabled: true }, Validators.required],
      tva: [this.costService?.tva, [Validators.required, Validators.pattern(RegexConstants.TVA_REGEX)]],
      dateStart: [this.costService?.date_start],
      dateEnd: [this.costService?.date_end],
      frequenceType: [this.selectedFrequenceType],
      renewal: [this.selectedRenewel, Validators.required],
      renewalAmountChoice: [this.selectedRenewelChoice],
      renewalFrequenceType: [this.selectedRenewelFrequenceType],
      renewalPrixHt: [this.costService.renewal_prix_ht],
      renewalTva: [this.costService.renewal_tva],
      renewalPrixTtc: [{ value: this.costService.renewal_prix_ttc, disabled: true }]
    });
    this.disableForm(this.coastForm, this.viewMode);
    this.coastForm.get('prixTtc').disable();
    this.coastForm.get('renewalPrixTtc').disable();
  }

  private disableForm(form: FormGroup, disable: boolean): void {
    if (disable) {
      form.disable();
    } else {
      form.enable();
    }
  }

  public editCostService(): void {
    this.viewMode = false;
    this.initServiceConfigForm();
    this.initCoastForm();
    this.fillCostServiceForm();
    this.subscribeToFormChanges();
  }

  public getProcessNameList(callback?: () => void): void {
    this.camundaService.getProcessByCategory(this.creationEntreprise).subscribe(data => {
      this.serviceNameList = data.map(item => ({
        id: item.id,
        label: item.name
      }));

      if (callback) {
        callback();
      }
    });
  }

  public handleCoastType(selectedCoast: any): void {
    this.tva = 0;
    this.prixHt = 0;
    if (selectedCoast.label === Constants.FIXE) {
      this.fixe = true;
      this.clearValidatorsForControl(this.coastForm.get('dateStart'));
      this.clearValidatorsForControl(this.coastForm.get('dateEnd'));
      this.clearValidatorsForControl(this.coastForm.get('frequenceType'));
    } else {
      this.fixe = false;
      this.setValidatorsForControl(this.coastForm.get('dateStart'), [Validators.required]);
      this.setValidatorsForControl(this.coastForm.get('dateEnd'), [Validators.required]);
      this.setValidatorsForControl(this.coastForm.get('frequenceType'), [Validators.required]);
    }
  }

  public calculateAmountTTC(prixHT: number, tva: number): number {
    return (Number(prixHT) + (Number(prixHT * tva) / 100));
  }

  public setValidatorsForControl(control: AbstractControl | null, validators: ValidatorFn[]): void {
    if (control) {
      control.setValidators(validators);
      control.updateValueAndValidity();
    }
  }

  public clearValidatorsForControl(control: AbstractControl | null): void {
    if (control) {
      control.clearValidators();
      control.markAsPristine();
      control.markAsUntouched();
      control.reset();
      control.updateValueAndValidity();
    }
  }

  public validateNumberInput(event: KeyboardEvent): boolean {
    const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
    const inputChar = event.key;

    if (allowedKeys.includes(inputChar)) {
      return true;
    }

    if (/^[0-9.]$/.test(inputChar)) {
      return true;
    }
    event.preventDefault();
    return false;
  }

  public subscribeToFormChanges(): void {
    this.serviceConfigForm.get('coastType')?.valueChanges.subscribe(selectedCoast => {
      if (selectedCoast === Constants.FIXE) {
        this.fixe = true;
      }
    });

    if (this.costService.serviceName != null) {
      this.prixHt = this.costService.prix_ht;
      this.tva = this.costService.tva;
    }

    this.coastForm.get('prixHt')?.valueChanges.subscribe(selectedCoast => {
      this.prixHt = selectedCoast;
      this.coastForm.get('prixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    });

    this.coastForm.get('tva')?.valueChanges.subscribe(selectedCoast => {
      this.tva = selectedCoast;
      this.coastForm.get('prixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    });

    if (this.costService.renewal_prix_ht != null) {
      this.renewel_prixHt = this.costService.renewal_prix_ht;
      this.renewel_tva = this.costService.renewal_tva;
    }

    this.coastForm.get('renewalPrixHt')?.valueChanges.subscribe(selectedCoast => {
      this.renewel_prixHt = selectedCoast;
      this.coastForm.get('renewalPrixTtc').setValue(this.calculateAmountTTC(this.renewel_prixHt, this.renewel_tva));
    });

    this.coastForm.get('renewalTva')?.valueChanges.subscribe(selectedCoast => {
      this.renewel_tva = selectedCoast;
      this.coastForm.get('renewalPrixTtc').setValue(this.calculateAmountTTC(this.renewel_prixHt, this.renewel_tva));
    });

    this.coastForm.get('renewal')?.valueChanges.subscribe(selectedCoast => {
      this.isRenewable = selectedCoast === Constants.RENEWABLE;
      if (this.isRenewable) {
        this.activateIfRenewableValidators();
      } else {
        this.desactivateIfConfigureRenewelAmount();
        this.desactivateIfRenewableValidators();
      }
    });

    this.coastForm.get('renewalAmountChoice')?.valueChanges.subscribe(selectedCoast => {
      this.keepAmount = selectedCoast === Constants.KEEP_SAME_AMOUNT;
      if (this.keepAmount) {
        this.desactivateIfConfigureRenewelAmount();
      } else {
        this.activateIfConfigureRenewelAmount();
      }
    });
  }

  public getCostService(costServiceId: string): void {
    this.configCostService.getCostServiceById(costServiceId).subscribe(
      data => {
        this.costService = data;
        this.updateFormWithCostServiceData();
      },
      err => this.handleError(err, 'failed_to_update_cost_service')
    );
  }

  private updateFormWithCostServiceData(): void {
    let costType = '';
    let renewelChoiceAmount = '';

    if (this.costService.isFixed) {
      this.fixe = true;
      costType = 'Fixe';
    } else {
      this.fixe = false;
      costType = 'Variable';
    }

    this.prixHt = this.costService.prix_ht;
    this.tva = this.costService.tva;
    this.selectedServiceName = this.serviceNameList.find(
      (el) => el.label === this.costService.serviceName
    );

    if (this.costService.isRenewable) {
      this.isRenewable = true;
      this.selectedRenewel = this.renewalTypeList.find(
        (el) => el.id === Constants.RENEWABLE
      );
      this.selectedRenewelFrequenceType = this.frequenceTypeList.find(
        (el) => el.id === this.costService.renewalFrequenceType
      );
    } else {
      this.isRenewable = false;
      this.selectedRenewel = this.renewalTypeList.find(
        (el) => el.id === Constants.NOT_RENEWABLE
      );
    }

    this.selectedFrequenceType = this.frequenceTypeList.find(
      (el) => el.id === this.costService.frequenceType
    );

    if (this.costService.renewal_prix_ht != null) {
      this.renewel_tva = this.costService.renewal_tva;
      this.renewel_prixHt = this.costService.renewal_prix_ht;
      this.keepAmount = false;
      renewelChoiceAmount = Constants.CONFIGURE_NEW_AMOUNT;
    } else {
      this.keepAmount = true;
      renewelChoiceAmount = Constants.KEEP_SAME_AMOUNT;
    }

    this.selectedRenewelChoice = this.renewalAmountChoiceList.find(
      (el) => el.id == renewelChoiceAmount
    );

    this.selectedCostType = this.costServiceTypeList.find(
      (el) => el.id === costType
    );

    this.initServiceConfigForm();
    this.initCoastForm();
    this.fillCostServiceForm();
    this.subscribeToFormChanges();
  }

  public fillCostServiceForm(): void {
    if (!this.costService) return;

    this.serviceConfigForm.patchValue({
      serviceName: this.selectedServiceName,
      coastType: this.selectedCostType,
    }, { emitEvent: false });

    this.coastForm.patchValue({
      prixHt: this.costService.prix_ht ?? 0,
      tva: this.costService.tva ?? 0,
      prixTtc: this.costService.prix_ttc ?? 0,
      frequenceType: this.costService.frequenceType,
      renewal: this.isRenewable ? Constants.RENEWABLE : Constants.NOT_RENEWABLE,
      renewalFrequenceType: this.costService.renewalFrequenceType,
      renewalAmountChoice: this.keepAmount ? Constants.KEEP_SAME_AMOUNT : Constants.CONFIGURE_NEW_AMOUNT,
      renewalPrixHt: this.costService.renewal_prix_ht,
      renewalTva: this.costService.renewal_tva,
      renewalPrixTtc: this.costService.renewal_prix_ttc,
    }, { emitEvent: false });

    this.updateDateEndMin();
  }

  public fillCostService(): void {
    if (this.coastForm && this.serviceConfigForm) {
      this.costService.categorieService = Constants.CREATION_ENTREPRISE;
      this.costService.serviceName = this.serviceConfigForm.get('serviceName').value.label;
      this.costService.isFixed = this.serviceConfigForm.get('coastType').value.label === Constants.FIXE;

      if (this.coastForm.get('renewal').value === Constants.RENEWABLE) {
        this.fillRenewableInformations();
      } else {
        this.costService.isRenewable = false;
      }

      this.costService.prix_ttc = this.coastForm.get('prixTtc').value;
      this.costService.prix_ht = this.coastForm.get('prixHt').value;
      this.costService.tva = this.coastForm.get('tva').value;
      this.costService.frequenceType = this.coastForm.get('frequenceType').value;
      this.costService.date_start = this.coastForm.get('dateStart').value;
      this.costService.date_end = this.coastForm.get('dateEnd').value;
    }
  }

  private fillRenewableInformations(): void {
    this.costService.isRenewable = true;
    if (this.coastForm.get('renewalAmountChoice').value !== Constants.KEEP_SAME_AMOUNT) {
      this.costService.renewal_prix_ht = this.coastForm.get('renewalPrixHt').value;
      this.costService.renewal_tva = this.coastForm.get('renewalTva').value;
      this.costService.renewal_prix_ttc = this.coastForm.get('renewalPrixTtc').value;
    } else {
      this.costService.renewal_prix_ht = this.coastForm.get('prixHt').value;
      this.costService.renewal_tva = this.coastForm.get('tva').value;
      this.costService.renewal_prix_ttc = this.coastForm.get('prixTtc').value;
    }
    this.costService.renewalFrequenceType = this.coastForm.get('renewalFrequenceType').value;
  }

  public back(): void {
    this.ngOnInit();
    this.router.navigate(['/pages/bs-configuration'], { relativeTo: this.route });
  }

  public updateCostService(): void {
    this.fillCostService();
    this.configCostService.updateCostService(this.costService, this.costServiceId).subscribe(
      data => {
        this.toastrService.onSuccess(this.translatePipe.transform('configuration.success.update_cost_service'), this.translatePipe.transform('menu.SUCCESS'));
        this.coastForm.reset();
        this.serviceConfigForm.reset();
        this.bsConfig.notifyDeletion();
        this.back();
      },
      err => this.handleError(err, 'failed_to_update_cost_service')
    );
  }

  public deleteCostService(): void {
    this.configCostService.deleteCostServiceById(this.costServiceId).subscribe(
      data => {
        this.toastrService.onSuccess(this.translatePipe.transform('configuration.success.delete_cost_service'), this.translatePipe.transform('menu.SUCCESS'));
        this.back();
        this.bsConfig.notifyDeletion();
      },
      err => this.handleError(err, 'failed_to_delete_cost_service')
    );
  }

  private handleError(err: any, errorKey: string): void {
    let errorDetails: string[] = err.error.detail.split(',');
    if (errorDetails.length > 0) {
      errorDetails.forEach((error) => {
        this.toastrService.onError(this.translatePipe.transform('configuration.error.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
      });
    }
    this.toastrService.onError(
      this.translatePipe.transform('configuration.error.' + errorKey),
      this.translatePipe.transform('menu.ERROR')
    );
  }

  public updateDateEndMin(): void {
    const dateStartValue = this.coastForm.get('dateStart')?.value;
    if (dateStartValue) {
      this.minDateEnd = dateStartValue;
    } else {
      this.minDateEnd = null;
    }
  }
  private initAllLists(): void {
    this.subscriptions.add(
      this.costTypeTranslation.costTypeList$.subscribe((list) => {
        this.costServiceTypeList = list;
      })
    );
    this.subscriptions.add(
      this.frequenceTypeTranslation.frequenceTypeList$.subscribe((list) => {
        this.frequenceTypeList = list;
      })
    );
    this.subscriptions.add(
      this.renewalTypeTranslation.renewalTypeList$.subscribe((list) => {
        this.renewalTypeList = list;
      })
    );
    this.subscriptions.add(
      this.renewalAmountChoice.renewalAmountChoiceList$.subscribe((list) => {
        this.renewalAmountChoiceList = list;
      })
    );
  }

  public desactivateIfRenewableValidators(): void {
    this.clearValidatorsForControl(this.coastForm.get('renewalAmountChoice'));
    this.clearValidatorsForControl(this.coastForm.get('renewalFrequenceType'));
    this.coastForm.get('renewalAmountChoice')?.updateValueAndValidity();
    this.coastForm.get('renewalFrequenceType')?.updateValueAndValidity();
  }

  public desactivateIfConfigureRenewelAmount(): void {
    this.clearValidatorsForControl(this.coastForm.get('renewalTva'));
    this.clearValidatorsForControl(this.coastForm.get('renewalPrixHt'));
    this.coastForm.get('renewalPrixHt')?.updateValueAndValidity();
    this.coastForm.get('renewalTva')?.updateValueAndValidity();
    this.coastForm.get('renewalPrixTtc')?.updateValueAndValidity();
  }
  public activateIfRenewableValidators():void {
    this.setValidatorsForControl(this.coastForm.get('renewalAmountChoice'), [Validators.required]);
    this.setValidatorsForControl(this.coastForm.get('renewalFrequenceType'), [Validators.required]);

    this.coastForm.get('renewalAmountChoice')?.updateValueAndValidity();
    this.coastForm.get('renewalFrequenceType')?.updateValueAndValidity();
  }

  public activateIfConfigureRenewelAmount(): void {
    this.coastForm.get('renewalPrixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    this.setValidatorsForControl(this.coastForm.get('renewalTva'), [Validators.required, Validators.pattern(RegexConstants.TVA_REGEX)]);
    this.setValidatorsForControl(this.coastForm.get('renewalPrixHt'), [Validators.required]);
    this.coastForm.get('renewalPrixHt')?.updateValueAndValidity();
    this.coastForm.get('renewalTva')?.updateValueAndValidity();
    this.coastForm.get('renewalPrixTtc')?.updateValueAndValidity();
  }
}
