import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  Input,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-back-top',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./back-top.component.scss'],
  template: `
    <i #backTop class="fa fa-angle-up back-to-top "></i>
  `
})
export class BackTopComponent implements OnInit, AfterViewInit {

  @Input() position: number;
  @Input() showSpeed: number;
  @Input() moveSpeed: number;

  @ViewChild('backTop') private selector: ElementRef;

  ngOnInit(): void {
    this.position = 300;
    this.showSpeed = 400;
    this.moveSpeed = 400;
  }

  ngAfterViewInit(): void {
    this._onWindowScroll();
  }

  @HostListener('click')
  _onClick(): boolean {
    jQuery('html, body').animate({scrollTop: 0}, {duration: this.moveSpeed});
    return false;
  }

  @HostListener('window:scroll')
  _onWindowScroll(): void {
    const el = this.selector.nativeElement;
    window.scrollY > this.position ? jQuery(el).fadeIn(this.showSpeed) : jQuery(el).fadeOut(this.showSpeed);
  }
}
