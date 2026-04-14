import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfValueViewComponent } from './list-of-value-view.component';

describe('ListOfValueViewComponent', () => {
  let component: ListOfValueViewComponent;
  let fixture: ComponentFixture<ListOfValueViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListOfValueViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListOfValueViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
