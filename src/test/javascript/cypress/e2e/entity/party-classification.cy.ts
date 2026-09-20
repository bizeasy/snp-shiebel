import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PartyClassification e2e test', () => {
  const partyClassificationPageUrl = '/party-classification';
  let username: string;
  let password: string;
  const partyClassificationSample = { fromDate: '2023-12-14T20:32:48.816Z' };

  let partyClassification;
  let party;
  let partyClassificationGroup;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'hunger',
        organisationShortName: 'hm finally potentially',
        salutation: 'aside',
        firstName: 'Tiara',
        middleName: 'gadzooks',
        lastName: 'Goodwin',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'disbar',
        primaryEmail: 'bench',
        isTemporaryPassword: false,
        logoImageUrl: 'gladly confirm',
        profileImageUrl: 'geez fair majority',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/party-classification-groups',
      body: { name: 'coincide meh', description: 'milestone whereas bleakly' },
    }).then(({ body }) => {
      partyClassificationGroup = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-classifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-classifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-classifications/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/party-classification-groups', {
      statusCode: 200,
      body: [partyClassificationGroup],
    });
  });

  afterEach(() => {
    if (partyClassification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-classifications/${partyClassification.id}`,
      }).then(() => {
        partyClassification = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
    if (partyClassificationGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-classification-groups/${partyClassificationGroup.id}`,
      }).then(() => {
        partyClassificationGroup = undefined;
      });
    }
  });

  it('PartyClassifications menu should load PartyClassifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-classification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyClassification').should('exist');
    cy.location('pathname').should('eq', partyClassificationPageUrl);
  });

  describe('PartyClassification page', () => {
    it('should have translated page title', () => {
      cy.visit(partyClassificationPageUrl);
      cy.getEntityHeading('PartyClassification').should('not.contain', 'snpApp.partyClassification.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyClassificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyClassification page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyClassificationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyClassification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-classifications',
          body: {
            ...partyClassificationSample,
            party,
            classificationGroup: partyClassificationGroup,
          },
        }).then(({ body }) => {
          partyClassification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-classifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/party-classifications?page=0&size=20>; rel="last",<http://localhost/api/party-classifications?page=0&size=20>; rel="first"',
              },
              body: [partyClassification],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyClassificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyClassification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyClassification');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationPageUrl);
      });

      it('edit button click should load edit PartyClassification page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationPageUrl);
      });

      it('edit button click should load edit PartyClassification page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassification');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationPageUrl);
      });

      it('last delete button click should delete instance of PartyClassification', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyClassification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationPageUrl);

        partyClassification = undefined;
      });
    });
  });

  describe('new PartyClassification page', () => {
    beforeEach(() => {
      cy.visit(partyClassificationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyClassification');
    });

    it('should create an instance of PartyClassification', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-13T22:36');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-13T22:36');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T11:20');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T11:20');

      cy.get(`[data-cy="party"]`).select(1);
      cy.get(`[data-cy="classificationGroup"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyClassification = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyClassificationPageUrl);
    });
  });
});
