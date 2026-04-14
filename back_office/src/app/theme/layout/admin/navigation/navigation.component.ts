import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BSConfig} from '../../../../app-config';


@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {
    public windowWidth: number;
    public bSConfig: any;
    @Input() navCollapsed: boolean;
    @Output() onNavMobCollapse = new EventEmitter();

    constructor() {
        this.bSConfig = BSConfig.config;
        this.windowWidth = window.innerWidth;
    }

    ngOnInit() {}

    navMobCollapse() {
        if (this.windowWidth < 992) {
            this.onNavMobCollapse.emit();
        }
    }
}
