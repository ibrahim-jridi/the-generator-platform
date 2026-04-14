import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenewalRegionWaitingListComponent } from './renewal-region-waiting-list.component';

describe('RenewalRegionWaitingListComponent', () => {
  let component: RenewalRegionWaitingListComponent;
  let fixture: ComponentFixture<RenewalRegionWaitingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RenewalRegionWaitingListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RenewalRegionWaitingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
