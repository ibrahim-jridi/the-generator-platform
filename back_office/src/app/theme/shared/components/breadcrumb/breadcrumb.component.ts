import { Component, Input, OnInit } from '@angular/core';
import { NavigationItem } from '../../../layout/admin/navigation/navigation';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.scss']
})
export class BreadcrumbComponent implements OnInit {
  @Input() type: string;

  public navigation: any;
  breadcrumbList: Array<any> = [];
  public navigationList: Array<any> = [];

  constructor(private route: Router, public nav: NavigationItem, private titleService: Title, private translatePipe: TranslatePipe) {
    this.navigation = this.nav.get();
    this.type = 'theme2';
    this.setBreadcrumb();
  }

  ngOnInit() {
    let routerUrl: string;
    routerUrl = this.route.url;
    if (routerUrl && typeof routerUrl === 'string') {
      this.filterNavigation(routerUrl);
    }
  }

  setBreadcrumb() {
    let routerUrl: string;
    this.route.events.subscribe((router: any) => {
      routerUrl = router.urlAfterRedirects;
      if (routerUrl && typeof routerUrl === 'string') {
        this.breadcrumbList.length = 0;
        const activeLink = router.url;
        this.filterNavigation(activeLink);
      }
    });
  }


  filterNavigation(activeLink) {
    let result = [];
    let title = 'Welcome';
    this.navigation.forEach((a) => {
      if (a.type === 'item' && 'url' in a && a.url === activeLink) {
        result = [
          {
            url: 'url' in a ? a.url : false,
            title: a.title,
            breadcrumbs: 'breadcrumbs' in a ? a.breadcrumbs : true,
            type: a.type
          }
        ];
        title = a.title;
      } else {
        if (a.type === 'group' && 'children' in a) {
          a.children.forEach((b) => {
            if (b.type === 'item' && 'url' in b && b.url === activeLink) {
              result = [
                {
                  url: 'url' in b ? b.url : false,
                  title: b.title,
                  breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                  type: b.type
                }
              ];
              title = b.title;
            } else if (b.type === 'item' && 'children' in b) {
              b.children.forEach((x) => {
                if (x.type === 'item' && 'url' in x && (activeLink.includes(x.id) || (x.url.includes(activeLink) && x.url.includes('add')))) {
                  const urlSegments = activeLink.split('/');
                  const pageSegment = urlSegments[urlSegments.length - 2];
                  const lastSegment = urlSegments[urlSegments.length - 1];

                  if (lastSegment && x.id === pageSegment) {
                    let id: string;
                    if (lastSegment !== 'add') {
                      id = lastSegment;
                    } else {
                      id = '';
                    }

                    id = decodeURIComponent(id);
                    result = [
                      {
                        url: 'url' in b ? b.url : false,
                        title: b.title,
                        breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                        type: b.type
                      },
                      {
                        url: 'url' in x ? activeLink : false,
                        title: x.title + ' ' + ` ${id}`,
                        breadcrumbs: 'breadcrumbs' in x ? x.breadcrumbs : true,
                        type: x.type
                      }
                    ];
                    title = x.title + ' ' + ` ${id}`;
                  } else {
                    result = [
                      {
                        url: 'url' in b ? b.url : false,
                        title: b.title,
                        breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                        type: b.type
                      },
                      {
                        url: 'url' in x ? activeLink : false,
                        title: x.title,
                        breadcrumbs: 'breadcrumbs' in x ? x.breadcrumbs : true,
                        type: x.type
                      }
                    ];
                    title = x.title;
                  }
                }
              });
            } else {
              if (b.type === 'collapse' && 'children' in b) {
                b.children.forEach((c) => {
                  if (c.type === 'item' && 'children' in c) {
                    c.children.forEach((x) => {
                      if (x.type === 'item' && 'url' in x && (activeLink.includes(x.id) || (x.url.includes(activeLink) && x.url.includes('add')))) {
                        const urlSegments = activeLink.split('/');
                        const lastSegment = urlSegments[urlSegments.length - 1];
                        const modalUrl = x.url;
                        const modifiedUrl = modalUrl.replace(':id', lastSegment);
                        let modifiedActiveLink = activeLink.replace(/\/[^/]+$/, '');
                        modifiedActiveLink = urlSegments.join('/');
                        if (
                          lastSegment &&
                          (modifiedUrl === modifiedActiveLink ||
                            (/%20/.test(modifiedActiveLink) && modifiedUrl !== modifiedActiveLink.replace(/%20$/, '')))
                        ) {
                          let id: string = decodeURIComponent(lastSegment);
                          const pageSegment = urlSegments[urlSegments.length - 2];

                          if (lastSegment !== 'add') {
                            id = lastSegment;
                          } else {
                            id = '';
                          }
                          result = [
                            {
                              url: 'url' in b ? b.url : false,
                              title: b.title,
                              breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                              type: b.type
                            },
                            {
                              url: 'url' in c ? c.url : false,
                              title: c.title,
                              breadcrumbs: 'breadcrumbs' in c ? c.breadcrumbs : true,
                              type: c.type
                            },
                            {
                              url: 'url' in x ? activeLink : false,
                              title: x.title + ' ' + decodeURIComponent(` ${id}`),
                              breadcrumbs: 'breadcrumbs' in x ? x.breadcrumbs : true,
                              type: x.type
                            }
                          ];
                          title = x.title + ' ' + ` ${id}`;
                        } else {
                          result = [
                            {
                              url: 'url' in b ? b.url : false,
                              title: b.title,
                              breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                              type: b.type
                            },
                            {
                              url: 'url' in c ? c.url : false,
                              title: c.title,
                              breadcrumbs: 'breadcrumbs' in c ? c.breadcrumbs : true,
                              type: c.type
                            },
                            {
                              url: 'url' in x ? activeLink : false,
                              title: x.title,
                              breadcrumbs: 'breadcrumbs' in x ? x.breadcrumbs : true,
                              type: x.type
                            }
                          ];
                          title = x.title;
                        }
                      }
                    });
                  }
                  if (c.type === 'item' && 'url' in c && c.url === activeLink) {
                    result = [
                      {
                        url: 'url' in b ? b.url : false,
                        title: b.title,
                        breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                        type: b.type
                      },
                      {
                        url: 'url' in c ? c.url : false,
                        title: c.title,
                        breadcrumbs: 'breadcrumbs' in c ? c.breadcrumbs : true,
                        type: c.type
                      }
                    ];
                    title = c.title;
                  } else {
                    if (c.type === 'collapse' && 'children' in c) {
                      c.children.forEach((d) => {
                        if (d.type === 'item' && 'url' in d && d.url === activeLink) {
                          result = [
                            {
                              url: 'url' in b ? b.url : false,
                              title: b.title,
                              breadcrumbs: 'breadcrumbs' in b ? b.breadcrumbs : true,
                              type: b.type
                            },
                            {
                              url: 'url' in c ? c.url : false,
                              title: c.title,
                              breadcrumbs: 'breadcrumbs' in c ? c.breadcrumbs : true,
                              type: c.type
                            },
                            {
                              url: 'url' in d ? d.url : false,
                              title: d.title,
                              breadcrumbs: 'breadcrumbs' in c ? d.breadcrumbs : true,
                              type: d.type
                            }
                          ];
                          title = d.title;
                        }
                      });
                    }
                  }
                });
              }
            }
          });
        }
      }
    });
    this.navigationList = result;
    this.titleService.setTitle(this.translateTitle(title));
  }

  public translateTitle(breadcrumbTitle: string): string {
    if (breadcrumbTitle.includes(' ')) {
      const parts = breadcrumbTitle.split(' ', 2);
      const translatedPart = this.translatePipe.transform(parts[0]);
      const remainingPart = breadcrumbTitle.slice(parts[0].length).trim();

      if (!remainingPart) {
        return translatedPart;
      } else {
        return translatedPart + ' - ' + remainingPart;
      }
    } else {
      return this.translatePipe.transform(breadcrumbTitle);
    }
  }
}
