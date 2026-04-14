import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmnListComponent } from './dmn-list.component';

describe('DmnListComponent', () => {
  let component: DmnListComponent;
  let fixture: ComponentFixture<DmnListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DmnListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DmnListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
