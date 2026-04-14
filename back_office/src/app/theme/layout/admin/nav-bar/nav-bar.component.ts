import {Component, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {BSConfig} from "../../../../app-config";


@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {
  @Input()
  public hideLogo: boolean;
  @Input()
  public navCollapsed: boolean;
  public isFixedLogo: boolean;
  public nextConfig: any;
  public menuClass: boolean;
  public collapseStyle: string;
  public windowWidth: number;
  private heightToHideLogo = 40;
  @Output()
  public onNavCollapse = new EventEmitter();
  @Output()
  public onNavHeaderMobCollapse = new EventEmitter();

  constructor() {
    this.nextConfig = BSConfig.config;
    this.menuClass = false;
    this.collapseStyle = 'none';
    this.windowWidth = window.innerWidth;
  }

  ngOnInit(): void {
    this.initFixedLogo();
  }

  toggleMobOption(): void {
    this.menuClass = !this.menuClass;
    this.collapseStyle = this.menuClass ? 'block' : 'none';
  }

  navCollapse(): void {
    if( this.collapseStyle == 'block')
    {
      this.toggleMobOption()
    }
    if (this.windowWidth >= 992) {
      this.onNavCollapse.emit();
    } else {
      this.onNavHeaderMobCollapse.emit();
    }
  }

  initFixedLogo(): void {
    if (!this.navCollapsed && this.windowWidth >= 992) {
      this.isFixedLogo = true;
    } else {
      this.isFixedLogo = false;
    }
  }

  @HostListener('window:scroll')
  _onWindowScroll(): void {
    if (this.navCollapsed && window.scrollY > this.heightToHideLogo) {
      this.hideLogo = true;
      this.isFixedLogo = false;
    } else if (this.navCollapsed) {
      this.hideLogo = false;
      this.isFixedLogo = true;
    }
  }
}
