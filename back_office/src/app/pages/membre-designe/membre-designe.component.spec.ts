import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembreDesigneComponent } from './membre-designe.component';

describe('MembreDesigneComponent', () => {
  let component: MembreDesigneComponent;
  let fixture: ComponentFixture<MembreDesigneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembreDesigneComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MembreDesigneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
