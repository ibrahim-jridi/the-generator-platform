import {Component, OnInit, ViewChild} from '@angular/core';
import {
    AbstractControl,
    FormBuilder,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    ValidatorFn,
    Validators
} from "@angular/forms";
import {BsConfigService} from "../../../../shared/services/bs-config.service";
import {CostService} from "../../../../shared/models/cost-service";
import {AppToastNotificationService} from "../../../../shared/services/appToastNotification.service";
import {TranslatePipe} from "@ngx-translate/core";
import {CamundaService} from "../../../../shared/services/camunda.service";
import {FrequenceType} from "../../../../shared/services/frequence-type";
import {Subscription} from "rxjs";
import {CostType} from "../../../../shared/services/cost-type";
import {ListCostServicesComponent} from "../list-cost-services/list-cost-services.component";
import {Constants} from "../../../../shared/utils/constants";
import {RegexConstants} from "../../../../shared/utils/regex-constants";
import {ActivatedRoute, Router} from "@angular/router";
import {RenewalType} from "../../../../shared/services/renewal-type";
import {RenewalAmountChoice} from "../../../../shared/services/renewal-amount-choice";
import {DetailCostServiceComponent} from "../detail-cost-service/detail-cost-service.component";

@Component({
    selector: 'app-cost-service-creation',
    templateUrl: './cost-service-creation.component.html',
    styleUrl: './cost-service-creation.component.scss'
})
export class CostServiceCreationComponent implements OnInit {
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
  public isConfigCollapsed = true;
  public isHistoryCollapsed = true;
  protected readonly Constants = Constants;
  public minDateEnd: string | null = null;
  public showCostType: boolean = false;
  public showRenewalSection: boolean = false;

  @ViewChild(ListCostServicesComponent) listCostComponent!: ListCostServicesComponent;
  @ViewChild(DetailCostServiceComponent) detailCostComponent!: DetailCostServiceComponent;

  constructor(private fb: FormBuilder,
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
  ) {
  }

  ngOnInit(): void {
    this.systemDate = new Date();
    this.initAllLists();
    this.getProcessNameList();
    this.initServiceConfigForm();
    this.initCoastForm();
    this.subscribeToFormChanges();
  }

  private initAllLists(): void {
    this.subscriptions.add(
      this.costTypeTranslation.costTypeList$.subscribe((list) => {
        this.costServiceTypeList = list;
      })
    )
    this.subscriptions.add(
      this.frequenceTypeTranslation.frequenceTypeList$.subscribe((list) => {
        this.frequenceTypeList = list;
      })
    )
    this.subscriptions.add(
      this.renewalTypeTranslation.renewalTypeList$.subscribe((list) => {
        this.renewalTypeList = list;
      })
    )
    this.subscriptions.add(
      this.renewalAmountChoice.renewalAmountChoiceList$.subscribe((list) => {
        this.renewalAmountChoiceList = list;
      })
    )
  }

  public activateIfRenewableValidators(): void {
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

  public desactivateIfRenewableValidators():void {
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

  public initServiceConfigForm(): void {
    const costTpe = this.costServiceTypeList.find(el => el.label = 'fixe')
    this.serviceConfigForm = this.fb.group({
      serviceName: ['', Validators.required],
      coastType: [''],
    })
    this.serviceConfigForm.get('coastType').setValue(costTpe);
  }

  public initCoastForm(): void {
    this.coastForm = this.fb.group({
      prixHt: ['', Validators.required],
      prixTtc: [{value: '', disabled: true}, Validators.required],
      tva: ['', [Validators.required, Validators.pattern(RegexConstants.TVA_REGEX)]],
      dateStart: [''],
      dateEnd: [''],
      frequenceType: [''],
      renewal: [''],
      renewalAmountChoice: [''],
      renewalFrequenceType: [''],
      renewalPrixHt: [''],
      renewalTva: [''],
      renewalPrixTtc: [{value: '', disabled: true}]
    })
    this.coastForm.get('prixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
  }

  public getProcessNameList(): void {
    this.camundaService.getProcessByCategory(this.creationEntreprise).subscribe(data => {
      data.forEach((item) => {
        const processNameElm = {id: item.id, label: item.name};
        this.serviceNameList = data.map(item => ({
          id: item.id,
          label: item.name
        }));
      })
    })
  }

  public handleCoastType(selectedCoast: any): void {
    this.tva = 0
    this.prixHt = 0;
    if (selectedCoast.label === Constants.FIXE){
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

  public fillCostService(): void {
    if (this.coastForm && this.serviceConfigForm) {
      this.costService.categorieService = Constants.CREATION_ENTREPRISE;
      this.costService.serviceName = this.serviceConfigForm.get('serviceName')?.value?.label || '';
      if (this.serviceConfigForm.get('coastType').value.label == Constants.FIXE) {
        this.costService.isFixed = true;
      } else {
        this.costService.isFixed = false;
      }

      if (this.coastForm.get('renewal').value === Constants.RENEWABLE) {
        this.fillRenewableInformations();
      } else {
        this.costService.isRenewable = false;
      }
      this.costService.prix_ttc = this.coastForm.get('prixTtc').value;
      this.costService.prix_ht = this.coastForm.get('prixHt').value;
      this.costService.tva = this.coastForm.get('tva').value;
      this.costService.frequenceType = this.coastForm.get('frequenceType').value || null;
      this.costService.date_start = this.coastForm.get('dateStart').value || null;
      this.costService.date_end = this.coastForm.get('dateEnd').value || null;
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
    this.costService.renewalFrequenceType = this.coastForm.get('renewalFrequenceType').value || null;
  }

  public createCostService(): void {
    this.fillCostService();
    this.configCostService.createCostService(this.costService).subscribe(data => {
        this.toastrService.onSuccess(this.translatePipe.transform('configuration.success.success_to_add_cost_service'), this.translatePipe.transform('menu.SUCCESS'));
        this.coastForm.reset();
        this.serviceConfigForm.reset();

        if (this.listCostComponent) {
          this.listCostComponent.loadCostServices();
        }

      },
      err => {
        let errorDetails: string[] = err.error.detail.split(',');
        if (errorDetails.length > 0) {
          errorDetails.forEach((error) => {
            this.toastrService.onError(this.translatePipe.transform('configuration.error.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
          });
        }
        this.toastrService.onError(
          this.translatePipe.transform('configuration.error.failed_to_add_cost_service'),
          this.translatePipe.transform('menu.ERROR')
        );
      });
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

  private subscribeToFormChanges(): void {
    this.serviceConfigForm.get('coastType')?.valueChanges.subscribe(selectedCoast => {
      if (selectedCoast === Constants.FIXE) {
        this.fixe = true;
      }
    });
    this.coastForm.get('prixHt')?.valueChanges.subscribe(selectedCoast => {
      this.prixHt = selectedCoast;
      this.coastForm.get('prixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    })
    this.coastForm.get('tva')?.valueChanges.subscribe(selectedCoast => {
      this.tva = selectedCoast;
      this.coastForm.get('prixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    })
    this.coastForm.get('renewalPrixHt')?.valueChanges.subscribe(selectedCoast => {
      this.prixHt = selectedCoast;
      this.coastForm.get('renewalPrixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    })
    this.coastForm.get('renewalTva')?.valueChanges.subscribe(selectedCoast => {
      this.tva = selectedCoast;
      this.coastForm.get('renewalPrixTtc').setValue(this.calculateAmountTTC(this.prixHt, this.tva));
    })
    this.coastForm.get('renewal')?.valueChanges.subscribe(selectedCoast => {
      if (selectedCoast === Constants.RENEWABLE) {
        this.isRenewable = true;
      } else {
        this.isRenewable = false;
      }
      if (this.isRenewable) {
        this.activateIfRenewableValidators();
      } else {
        this.desactivateIfConfigureRenewelAmount();
        this.desactivateIfRenewableValidators();
      }

    });
    this.coastForm.get('renewalAmountChoice')?.valueChanges.subscribe(selectedCoast => {
      if (selectedCoast !== '' && selectedCoast !== null) {
        if (selectedCoast !== Constants.KEEP_SAME_AMOUNT) {
          this.keepAmount = false;
        } else {
          this.keepAmount = true;
        }
      } else {
        this.keepAmount = true;
      }
      if (this.keepAmount) {
        this.desactivateIfConfigureRenewelAmount();
      } else {
        this.activateIfConfigureRenewelAmount();
      }
    });

  }
  public isDetailRouteActive(): boolean {
    return this.router.url.includes('detail-cost-service');
  }
  public updateDateEndMin(): void {
    const dateStartValue = this.coastForm.get('dateStart')?.value;
    if (dateStartValue) {
      this.minDateEnd = dateStartValue;
    } else {
      this.minDateEnd = null;
    }
  }
}
