import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCostServiceComponent } from './detail-cost-service.component';

describe('DetailCostServiceComponent', () => {
  let component: DetailCostServiceComponent;
  let fixture: ComponentFixture<DetailCostServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailCostServiceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailCostServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
