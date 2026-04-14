import {Component, NgZone, OnInit, ViewEncapsulation} from '@angular/core';
import {BSConfig} from '../../../../app-config';
import {Location} from '@angular/common';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConfigurationComponent implements OnInit {
  public styleSelectorToggle: boolean; // open configuration menu
  public layoutType: string; // layout type
  public rtlLayout: any; // rtl type
  public menuFixedLayout: any; // menu/navbar fixed flag
  public headerFixedLayout: any; // header fixed flag
  public boxLayout: any; // box layout flag
  public headerBackgroundColor: string; // header background color

  public headerBackColor: string;

  public bSConfig: any;
  public isConfig: boolean;

  scroll = (): void => {
    if (this.headerFixedLayout === false) {
      (document.querySelector('#nav-ps-next') as HTMLElement).style.maxHeight = 'calc(100vh)';
      const el = document.querySelector('.pcoded-navbar.menupos-fixed') as HTMLElement;
      const scrollPosition = window.pageYOffset;
      if (scrollPosition > 60) {
        el.style.position = 'fixed';
        el.style.transition = 'none';
        el.style.marginTop = '0';
      } else {
        el.style.position = 'absolute';
        el.style.marginTop = '60px';
      }
    } else if (document.querySelector('.pcoded-navbar').hasAttribute('style')) {
      document.querySelector('.pcoded-navbar.menupos-fixed').removeAttribute('style');
    }
  };

  constructor(private zone: NgZone, private location: Location) {
    this.bSConfig = BSConfig.config;
    this.setThemeLayout();
  }

  ngOnInit(): void {
    this.styleSelectorToggle = false;

    this.layoutType = this.bSConfig.layoutType;
    this.setLayout(this.layoutType);

    this.headerBackgroundColor = this.bSConfig.headerBackColor;
    this.setHeaderBackground(this.headerBackgroundColor);

    this.rtlLayout = this.bSConfig.rtlLayout;
    this.changeRtlLayout(this.rtlLayout);

    this.menuFixedLayout = this.bSConfig.navFixedLayout;
    if (this.bSConfig.layout === 'vertical') {
      this.changeMenuFixedLayout(this.menuFixedLayout);
    }
    this.headerFixedLayout = !this.bSConfig.headerFixedLayout;
    this.changeHeaderFixedLayout(this.headerFixedLayout);
    this.boxLayout = this.bSConfig.boxLayout;
    this.changeBoxLayout(this.boxLayout);
  }

  setThemeLayout() {
    let currentURL = this.location.path();
    const baseHref = this.location['_baseHref'];
    if (baseHref) {
      currentURL = baseHref + this.location.path();
    }

    switch (currentURL) {
      case baseHref + '/layout/static':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.navFixedLayout = false;
        this.bSConfig.headerFixedLayout = false;
        break;
      case baseHref + '/layout/fixed':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.navFixedLayout = true;
        this.bSConfig.headerFixedLayout = true;
        break;
      case baseHref + '/layout/nav-fixed':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.navFixedLayout = true;
        this.bSConfig.headerFixedLayout = false;
        break;
      case baseHref + '/layout/collapse-menu':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.collapseMenu = true;
        break;
      case baseHref + '/layout/vertical-rtl':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.rtlLayout = true;
        break;
      case baseHref + '/layout/horizontal':
        this.bSConfig.layout = 'horizontal';
        this.bSConfig.navFixedLayout = false;
        this.bSConfig.headerFixedLayout = false;
        this.bSConfig.collapseMenu = false;
        break;
      case baseHref + '/layout/horizontal-l2':
        this.bSConfig.layout = 'horizontal';
        this.bSConfig.subLayout = 'horizontal-2';
        this.bSConfig.navFixedLayout = false;
        this.bSConfig.headerFixedLayout = false;
        this.bSConfig.collapseMenu = false;
        break;
      case baseHref + '/layout/horizontal-rtl':
        this.bSConfig.layout = 'horizontal';
        this.bSConfig.subLayout = 'horizontal-2';
        this.bSConfig.navFixedLayout = false;
        this.bSConfig.headerFixedLayout = false;
        this.bSConfig.rtlLayout = true;
        this.bSConfig.collapseMenu = false;
        break;
      case baseHref + '/layout/box':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.boxLayout = true;
        this.bSConfig.navFixedLayout = true;
        this.bSConfig.headerFixedLayout = false;
        this.bSConfig.collapseMenu = true;
        break;
      case baseHref + '/layout/light':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.layoutType = 'menu-light';
        this.bSConfig.headerBackColor = 'background-blue';
        break;
      case baseHref + '/layout/dark':
        this.bSConfig.layout = 'vertical';
        this.bSConfig.layoutType = 'dark';
        this.bSConfig.headerBackColor = 'background-blue';
        break;
      default:
        break;
    }
  }

  setHeaderBackColor(color) {
    this.headerBackColor = color;
    (document.querySelector('body') as HTMLElement).style.background = color;
  }

  // change main layout
  setLayout(layout) {
    this.isConfig = !this.bSConfig.showdemo;

    document.querySelector('.pcoded-navbar').classList.remove('menu-light');
    document.querySelector('.pcoded-navbar').classList.remove('menu-dark');
    document.querySelector('body').classList.remove('able-pro-dark');

    this.layoutType = layout;
    if (layout === 'menu-light') {
      document.querySelector('.pcoded-navbar').classList.add(layout);
      this.setHeaderBackground('background-blue');
    }
    if (layout === 'dark') {
      this.setHeaderBackground('background-blue');
      document.querySelector('body').classList.add('able-pro-dark');
    }
    if (layout === 'reset') {
      this.reset();
    }
  }

  reset(): void {
    this.ngOnInit();
  }

  setRtlLayout(e): void {
    const flag = !!e.target.checked;
    this.changeRtlLayout(flag);
  }

  changeRtlLayout(flag): void {
    if (flag) {
      document.querySelector('body').classList.add('bs-theme-rtl');
    } else {
      document.querySelector('body').classList.remove('bs-theme-rtl');
    }
  }

  setMenuFixedLayout(e): void {
    const flag = !!e.target.checked;
    this.changeMenuFixedLayout(flag);
  }

  changeMenuFixedLayout(flag): void {
    setTimeout(() => {
      if (flag) {
        document.querySelector('.pcoded-navbar').classList.remove('menupos-static');
        document.querySelector('.pcoded-navbar').classList.add('menupos-fixed');
        if (this.bSConfig.layout === 'vertical') {
          (document.querySelector('#nav-ps-next') as HTMLElement).style.maxHeight = 'calc(100vh - 60px)'; // calc(100vh - 70px) amit
        }
        window.addEventListener('scroll', this.scroll, true);
        window.scrollTo(0, 0);
      } else {
        document.querySelector('.pcoded-navbar').classList.add('menupos-static');
        document.querySelector('.pcoded-navbar').classList.remove('menupos-fixed');
        if (this.bSConfig.layout === 'vertical') {
          (document.querySelector('#nav-ps-next') as HTMLElement).style.maxHeight = 'calc(100%)'; // calc(100% - 70px) amit
        }
        if (this.bSConfig.layout === 'vertical') {
          window.removeEventListener('scroll', this.scroll, true);
        }
      }
    }, 100);
  }

  setHeaderFixedLayout(e): void {
    const flag = !!e.target.checked;
    this.changeHeaderFixedLayout(flag);
  }

  changeHeaderFixedLayout(flag): void {
    if (flag) {
      document.querySelector('.pcoded-header').classList.add('headerpos-fixed');
    } else {
      document.querySelector('.pcoded-header').classList.remove('headerpos-fixed');
      // static
      if (this.bSConfig.layout === 'vertical' && this.menuFixedLayout) {
        window.addEventListener('scroll', this.scroll, true);
        window.scrollTo(0, 0);
      } else {
        window.removeEventListener('scroll', this.scroll, true);
      }
    }
  }

  setBoxLayout(e): void {
    const flag = !!e.target.checked;
    this.changeBoxLayout(flag);
  }

  changeBoxLayout(flag): void {
    if (flag) {
      document.querySelector('body').classList.add('container');
      document.querySelector('body').classList.add('box-layout');
    } else {
      document.querySelector('body').classList.remove('box-layout');
      document.querySelector('body').classList.remove('container');
    }
  }

  setHeaderBackground(background): void {
    this.headerBackgroundColor = background;
    document.querySelector('body').classList.remove('background-blue');
    document.querySelector('body').classList.remove('background-red');
    document.querySelector('body').classList.remove('background-purple');
    document.querySelector('body').classList.remove('background-info');
    document.querySelector('body').classList.remove('background-green');
    document.querySelector('body').classList.remove('background-dark');
    document.querySelector('body').classList.remove('background-grd-blue');
    document.querySelector('body').classList.remove('background-grd-red');
    document.querySelector('body').classList.remove('background-grd-purple');
    document.querySelector('body').classList.remove('background-grd-info');
    document.querySelector('body').classList.remove('background-grd-green');
    document.querySelector('body').classList.remove('background-grd-dark');
    document.querySelector('body').classList.remove('background-img-1');
    document.querySelector('body').classList.remove('background-img-2');
    document.querySelector('body').classList.remove('background-img-3');
    document.querySelector('body').classList.remove('background-img-4');
    document.querySelector('body').classList.remove('background-img-5');
    document.querySelector('body').classList.remove('background-img-6');

    document.querySelector('body').classList.add(background);
  }
}
