import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenewalCategoryWaitingListComponent } from './renewal-category-waiting-list.component';

describe('RenewalCategoryWaitingListComponent', () => {
  let component: RenewalCategoryWaitingListComponent;
  let fixture: ComponentFixture<RenewalCategoryWaitingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RenewalCategoryWaitingListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RenewalCategoryWaitingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
