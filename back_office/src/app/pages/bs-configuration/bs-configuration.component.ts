import {Component, OnInit} from '@angular/core';
import {Subject, Subscription} from "rxjs";
import {CategoryService} from "../../shared/services/category-service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-bs-configuration',
  templateUrl: './bs-configuration.component.html',
  styleUrl: './bs-configuration.component.scss'
})
export class BsConfigurationComponent implements OnInit {

  public active1: string = 'element-report';
  public active2: string = 'element-service';
  isNav1Collapsed = false;
  isNav2Collapsed = false;
  public selectedNav: string = 'nav1'
  selectedItemId: string | null = null; // Track selected item

  onSelectItem(itemId: string) {
    this.selectedItemId = itemId;
  }
  navItems = [
    { id: 'element-report', label: 'configuration.report.element-report' },
    { id: 'template-report', label: 'configuration.report.template-report' }
  ];
  private costServiceDeleted = new Subject<void>();

  public  costServiceDeleted$ = this.costServiceDeleted.asObservable();

  public notifyDeletion(): void {
    this.costServiceDeleted.next();
  }
  public categoryServiceItem: { id: string; label: any }[] = [];

  private subscriptions: Subscription = new Subscription();
  constructor(private categoryServices: CategoryService,
              private router: Router,
              private route: ActivatedRoute,) {
  }
  ngOnInit(): void {
    this.subscriptions.add(
      this.categoryServices.categoryList$.subscribe((list) => {
        this.categoryServiceItem = list;
      })
    )
  }
}
