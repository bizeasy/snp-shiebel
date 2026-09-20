import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISalesChannel } from '../sales-channel.model';
import { SalesChannelService } from '../service/sales-channel.service';

import { SalesChannelFormService } from './sales-channel-form.service';
import { SalesChannelUpdate } from './sales-channel-update';

describe('SalesChannel Management Update Component', () => {
  let comp: SalesChannelUpdate;
  let fixture: ComponentFixture<SalesChannelUpdate>;
  let activatedRoute: ActivatedRoute;
  let salesChannelFormService: SalesChannelFormService;
  let salesChannelService: SalesChannelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(SalesChannelUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salesChannelFormService = TestBed.inject(SalesChannelFormService);
    salesChannelService = TestBed.inject(SalesChannelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const salesChannel: ISalesChannel = { id: 32514 };

      activatedRoute.data = of({ salesChannel });
      comp.ngOnInit();

      expect(comp.salesChannel).toEqual(salesChannel);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISalesChannel>();
      const salesChannel = { id: 25587 };
      vi.spyOn(salesChannelFormService, 'getSalesChannel').mockReturnValue(salesChannel);
      vi.spyOn(salesChannelService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(salesChannel);
      saveSubject.complete();

      // THEN
      expect(salesChannelFormService.getSalesChannel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salesChannelService.update).toHaveBeenCalledWith(expect.objectContaining(salesChannel));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISalesChannel>();
      const salesChannel = { id: 25587 };
      vi.spyOn(salesChannelFormService, 'getSalesChannel').mockReturnValue({ id: null });
      vi.spyOn(salesChannelService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesChannel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(salesChannel);
      saveSubject.complete();

      // THEN
      expect(salesChannelFormService.getSalesChannel).toHaveBeenCalled();
      expect(salesChannelService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISalesChannel>();
      const salesChannel = { id: 25587 };
      vi.spyOn(salesChannelService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salesChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salesChannelService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
