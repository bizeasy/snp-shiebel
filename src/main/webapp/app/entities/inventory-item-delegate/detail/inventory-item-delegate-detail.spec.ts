import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { InventoryItemDelegateDetail } from './inventory-item-delegate-detail';

describe('InventoryItemDelegate Management Detail Component', () => {
  let comp: InventoryItemDelegateDetail;
  let fixture: ComponentFixture<InventoryItemDelegateDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./inventory-item-delegate-detail').then(m => m.InventoryItemDelegateDetail),
              resolve: { inventoryItemDelegate: () => of({ id: 14485 }) },
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
    fixture = TestBed.createComponent(InventoryItemDelegateDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load inventoryItemDelegate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InventoryItemDelegateDetail);

      // THEN
      expect(instance.inventoryItemDelegate()).toEqual(expect.objectContaining({ id: 14485 }));
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
