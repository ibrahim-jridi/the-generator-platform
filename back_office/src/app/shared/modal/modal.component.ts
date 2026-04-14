import { Component, OnDestroy, OnInit } from '@angular/core';
import { ModalService } from '../services/modal.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit, OnDestroy {
  header: string = '';
  icon: string = '';
  body: string = '';
  footer: string = '';
  dialogClass: string = '';
  hideHeader: boolean = false;
  hideIcon: boolean = false;
  hideFooter: boolean = false;
  containerClick: boolean = true;
  visible: boolean = false;
  visibleAnimate: boolean = false;

  private modalVisibilitySub: Subscription;
  private modalContentSub: Subscription;

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    this.modalVisibilitySub = this.modalService.modalVisibility$.subscribe((visible) => {
      this.visible = visible;
      if (this.visible) {
        setTimeout(() => (this.visibleAnimate = true), 100);
        document.body.classList.add('modal-open');
      } else {
        this.visibleAnimate = false;
        setTimeout(() => document.body.classList.remove('modal-open'), 300);
      }
    });

    this.modalContentSub = this.modalService.modalContent$.subscribe((content) => {
      this.header = content.header;
      this.icon = content.icon;
      this.body = content.body;
      this.footer = content.footer;
      this.dialogClass = content.dialogClass;
      this.hideHeader = content.hideHeader;
      this.hideIcon = content.hideIcon;
      this.hideFooter = content.hideFooter;
      this.containerClick = content.containerClick;
    });
  }

  ngOnDestroy() {
    if (this.modalVisibilitySub) {
      this.modalVisibilitySub.unsubscribe();
    }
    if (this.modalContentSub) {
      this.modalContentSub.unsubscribe();
    }
  }

  hide(): void {
    this.modalService.hideModal();
  }

  onContainerClicked(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('modal') && this.containerClick) {
      this.hide();
    }
  }
}
