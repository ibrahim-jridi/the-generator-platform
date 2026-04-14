import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { NgbDropdownConfig } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-custom-button',
  templateUrl: './custom-button.component.html',
  styleUrls: ['./custom-button.component.scss'],
  providers: [NgbDropdownConfig]
})
export class CustomButtonComponent implements OnInit {
  @Input() buttonText: string;
  @Input() buttonClass: string;
  @Input() tooltip: string;
  @Input() buttonIcon: string;
  @Input() buttonType: string;
  @Input() buttonDisabled: boolean;
  @Input() buttonIconClass: string;
  @Input() buttonIconAlt: string;
  @Output() clicked: EventEmitter<any> = new EventEmitter<any>();
  isRtl: boolean;

  constructor(config: NgbDropdownConfig) {
    config.placement = 'bottom-right';
    this.buttonText = '';
    this.buttonClass = '';
    this.buttonIcon = '';
    this.buttonType = 'button';
    this.buttonDisabled = false;
    this.buttonIconClass = '';
    this.buttonIconAlt = '';
  }

  ngOnInit(): void {
    this.isRtl = navigator.language === 'ar';
  }

  onClick(): void {
    this.clicked.emit();
  }

  isFontAwesomeIcon(): boolean {
    return this.buttonIcon.startsWith('fas') || this.buttonIcon.startsWith('icon');
  }

  isImageIcon(): boolean {
    return this.buttonIcon.startsWith('http://') || this.buttonIcon.startsWith('https://') || this.buttonIcon.endsWith('.png') || this.buttonIcon.endsWith('.jpg') || this.buttonIcon.endsWith('.jpeg') || this.buttonIcon.endsWith('.gif') || this.buttonIcon.endsWith('.svg');
  }

  showIcon(): boolean {
    return this.isFontAwesomeIcon() || this.isImageIcon();
  }
}
