import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { InventoryItemVarianceDetail } from './inventory-item-variance-detail';

describe('InventoryItemVariance Management Detail Component', () => {
  let comp: InventoryItemVarianceDetail;
  let fixture: ComponentFixture<InventoryItemVarianceDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./inventory-item-variance-detail').then(m => m.InventoryItemVarianceDetail),
              resolve: { inventoryItemVariance: () => of({ id: 7138 }) },
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
    fixture = TestBed.createComponent(InventoryItemVarianceDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load inventoryItemVariance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InventoryItemVarianceDetail);

      // THEN
      expect(instance.inventoryItemVariance()).toEqual(expect.objectContaining({ id: 7138 }));
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
