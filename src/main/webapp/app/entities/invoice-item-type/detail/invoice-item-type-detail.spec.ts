import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { InvoiceItemTypeDetail } from './invoice-item-type-detail';

describe('InvoiceItemType Management Detail Component', () => {
  let comp: InvoiceItemTypeDetail;
  let fixture: ComponentFixture<InvoiceItemTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./invoice-item-type-detail').then(m => m.InvoiceItemTypeDetail),
              resolve: { invoiceItemType: () => of({ id: 32383 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceItemTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load invoiceItemType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InvoiceItemTypeDetail);

      // THEN
      expect(instance.invoiceItemType()).toEqual(expect.objectContaining({ id: 32383 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
