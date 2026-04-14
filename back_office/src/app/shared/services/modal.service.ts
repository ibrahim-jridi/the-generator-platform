import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private modalVisibilitySource = new BehaviorSubject<boolean>(false);
  private modalContentSource = new BehaviorSubject<{
    header: string;
    icon: string;
    body: string;
    footer: string;
    dialogClass: string;
    hideHeader: boolean;
    hideIcon: boolean;
    hideFooter: boolean;
    containerClick: boolean;
  }>({
    header: '',
    icon: '',
    body: '',
    footer: '',
    dialogClass: '',
    hideHeader: false,
    hideIcon: false,
    hideFooter: false,
    containerClick: true
  });

  modalVisibility$ = this.modalVisibilitySource.asObservable();
  modalContent$ = this.modalContentSource.asObservable();

  constructor() {}

  showModal(config: {
    header: string;
    body: string;
    icon: string;
    footer: string;
    dialogClass?: string;
    hideHeader?: boolean;
    hideIcon?: boolean;
    hideFooter?: boolean;
    containerClick?: boolean;
  }): void {
    const currentConfig = this.modalContentSource.getValue();
    this.modalContentSource.next({
      header: config.header || currentConfig.header,
      icon: config.icon || currentConfig.icon,
      body: config.body || currentConfig.body,
      footer: config.footer || currentConfig.footer,
      dialogClass: config.dialogClass || currentConfig.dialogClass,
      hideHeader: config.hideHeader ?? currentConfig.hideHeader,
      hideIcon: config.hideIcon ?? currentConfig.hideIcon,
      hideFooter: config.hideFooter ?? currentConfig.hideFooter,
      containerClick: config.containerClick ?? currentConfig.containerClick
    });
    this.modalVisibilitySource.next(true);
  }

  hideModal(): void {
    this.modalVisibilitySource.next(false);
  }
}
