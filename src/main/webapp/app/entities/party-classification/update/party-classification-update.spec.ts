import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyClassificationGroup } from 'app/entities/party-classification-group/party-classification-group.model';
import { PartyClassificationGroupService } from 'app/entities/party-classification-group/service/party-classification-group.service';
import { IPartyClassification } from '../party-classification.model';
import { PartyClassificationService } from '../service/party-classification.service';

import { PartyClassificationFormService } from './party-classification-form.service';
import { PartyClassificationUpdate } from './party-classification-update';

describe('PartyClassification Management Update Component', () => {
  let comp: PartyClassificationUpdate;
  let fixture: ComponentFixture<PartyClassificationUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyClassificationFormService: PartyClassificationFormService;
  let partyClassificationService: PartyClassificationService;
  let partyService: PartyService;
  let partyClassificationGroupService: PartyClassificationGroupService;

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

    fixture = TestBed.createComponent(PartyClassificationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyClassificationFormService = TestBed.inject(PartyClassificationFormService);
    partyClassificationService = TestBed.inject(PartyClassificationService);
    partyService = TestBed.inject(PartyService);
    partyClassificationGroupService = TestBed.inject(PartyClassificationGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const partyClassification: IPartyClassification = { id: 12846 };
      const party: IParty = { id: 24527 };
      partyClassification.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyClassification });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PartyClassificationGroup query and add missing value', () => {
      const partyClassification: IPartyClassification = { id: 12846 };
      const classificationGroup: IPartyClassificationGroup = { id: 8459 };
      partyClassification.classificationGroup = classificationGroup;

      const partyClassificationGroupCollection: IPartyClassificationGroup[] = [{ id: 8459 }];
      vi.spyOn(partyClassificationGroupService, 'query').mockReturnValue(
        of(new HttpResponse({ body: partyClassificationGroupCollection })),
      );
      const additionalPartyClassificationGroups = [classificationGroup];
      const expectedCollection: IPartyClassificationGroup[] = [
        ...additionalPartyClassificationGroups,
        ...partyClassificationGroupCollection,
      ];
      vi.spyOn(partyClassificationGroupService, 'addPartyClassificationGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyClassification });
      comp.ngOnInit();

      expect(partyClassificationGroupService.query).toHaveBeenCalled();
      expect(partyClassificationGroupService.addPartyClassificationGroupToCollectionIfMissing).toHaveBeenCalledWith(
        partyClassificationGroupCollection,
        ...additionalPartyClassificationGroups.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partyClassificationGroupsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyClassification: IPartyClassification = { id: 12846 };
      const party: IParty = { id: 24527 };
      partyClassification.party = party;
      const classificationGroup: IPartyClassificationGroup = { id: 8459 };
      partyClassification.classificationGroup = classificationGroup;

      activatedRoute.data = of({ partyClassification });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.partyClassificationGroupsSharedCollection()).toContainEqual(classificationGroup);
      expect(comp.partyClassification).toEqual(partyClassification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassification>();
      const partyClassification = { id: 21352 };
      vi.spyOn(partyClassificationFormService, 'getPartyClassification').mockReturnValue(partyClassification);
      vi.spyOn(partyClassificationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassification);
      saveSubject.complete();

      // THEN
      expect(partyClassificationFormService.getPartyClassification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyClassificationService.update).toHaveBeenCalledWith(expect.objectContaining(partyClassification));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassification>();
      const partyClassification = { id: 21352 };
      vi.spyOn(partyClassificationFormService, 'getPartyClassification').mockReturnValue({ id: null });
      vi.spyOn(partyClassificationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassification);
      saveSubject.complete();

      // THEN
      expect(partyClassificationFormService.getPartyClassification).toHaveBeenCalled();
      expect(partyClassificationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassification>();
      const partyClassification = { id: 21352 };
      vi.spyOn(partyClassificationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyClassificationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePartyClassificationGroup', () => {
      it('should forward to partyClassificationGroupService', () => {
        const entity = { id: 8459 };
        const entity2 = { id: 28249 };
        vi.spyOn(partyClassificationGroupService, 'comparePartyClassificationGroup');
        comp.comparePartyClassificationGroup(entity, entity2);
        expect(partyClassificationGroupService.comparePartyClassificationGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
