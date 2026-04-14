import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

import {SizeConfigService} from "../../../shared/services/size-config.service";
import {Router} from "@angular/router";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {AppToastNotificationService} from "../../../shared/services/appToastNotification.service";
import {SizeConfig} from "../../../shared/models/sizeConfig.model";

@Component({
  selector: 'app-update-configuration-ged',
  templateUrl: './update-configuration-ged.component.html',
  styleUrl: './update-configuration-ged.component.scss'
})
export class UpdateConfigurationGedComponent implements OnInit {
  public sizeConfigForm: FormGroup;
  protected sizeConfig: SizeConfig;
  protected sizeConfigId: string;
  protected state: any;
  protected sizeConfigs: SizeConfig[];

  constructor(
    private sizeConfigService: SizeConfigService,
    private formBuilder: FormBuilder,
    private router: Router,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private translateService: TranslateService
  ) {
  }

  public ngOnInit(): void {
    this.state = window.history.state;
    this.sizeConfigId = this.state.sizeConfigId;
    this.loadSizeConfigs();
    this.initSizeConfigForm();
    this.handleOnGetSizeConfigUpdate(this.sizeConfigId);
  }

  private initSizeConfigForm(): void {
    this.sizeConfigForm = this.formBuilder.group({
      extension: ['', Validators.required],
      maxSize: ['', Validators.required]
    });
  }

  private loadSizeConfigs(): void {
    this.sizeConfigService.getSizeConfigs().subscribe({
      next: (response: any) => {
        this.sizeConfigs = response || [];
        this.handleOnGetSizeConfigUpdate(this.sizeConfigId);
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('sizeConfig.FAILED_TO_LOAD_SIZE_CONFIG'), this.translatePipe.transform('menu.ERROR'));
      },
    });
  }

  public handleOnGetSizeConfigUpdate(id: any): void {
    this.sizeConfigService.getSizeConfigById(id).subscribe({
      next: ((data: SizeConfig) => {
        this.sizeConfig = data;
        this.patchFormDataSizeConfig();
      }),
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('sizeConfig.FAILED_TO_LOAD_SIZE_CONFIG'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private patchFormDataSizeConfig(): void {
    this.sizeConfigForm.patchValue({
      extension: this.sizeConfig.extension,
      maxSize: this.sizeConfig.maxSize
    });
    this.validateForm();
  }

  private validateForm(): void {
    Object.keys(this.sizeConfigForm.controls).forEach(field => {
      const control = this.sizeConfigForm.get(field);
      control.markAsTouched({onlySelf: true});
    });
  }

  private fillSizeConfigData(): void {
    this.sizeConfig.extension = this.sizeConfigForm.value.extension;
    this.sizeConfig.maxSize = this.sizeConfigForm.value.maxSize;
  }

  public updateSizeConfig(): void {
    this.fillSizeConfigData();
    this.sizeConfigService.updateSizeConfig(this.sizeConfigId, this.sizeConfig).subscribe({
        next: () => {
          this.toastrService.onSuccess(this.translatePipe.transform('sizeConfig.SUCCESS_TO_UPDATE_SIZE_CONFIG'), this.translatePipe.transform('menu.SUCCESS'));
          this.router.navigate(['/pages/configuration-ged']);
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('sizeConfig.FAILED_TO_UPDATE_SIZE_CONFIG'), this.translatePipe.transform('menu.ERROR'));
        }
      }
    );
  }

  public back(): void {
    this.router.navigate(['/pages/configuration-ged']);
  }
}
