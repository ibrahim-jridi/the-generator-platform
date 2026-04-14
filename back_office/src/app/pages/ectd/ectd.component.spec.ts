import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EctdComponent } from './ectd.component';

describe('EctdComponent', () => {
  let component: EctdComponent;
  let fixture: ComponentFixture<EctdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EctdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EctdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
