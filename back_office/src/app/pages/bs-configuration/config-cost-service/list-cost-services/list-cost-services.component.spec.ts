import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListCostServicesComponent } from './list-cost-services.component';

describe('ListCostServicesComponent', () => {
  let component: ListCostServicesComponent;
  let fixture: ComponentFixture<ListCostServicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListCostServicesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListCostServicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
