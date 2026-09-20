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

describe('PartyRelationshipType e2e test', () => {
  const partyRelationshipTypePageUrl = '/party-relationship-type';
  let username: string;
  let password: string;
  const partyRelationshipTypeSample = { name: 'the uh-huh' };

  let partyRelationshipType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-relationship-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-relationship-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-relationship-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyRelationshipType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-relationship-types/${partyRelationshipType.id}`,
      }).then(() => {
        partyRelationshipType = undefined;
      });
    }
  });

  it('PartyRelationshipTypes menu should load PartyRelationshipTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-relationship-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyRelationshipType').should('exist');
    cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
  });

  describe('PartyRelationshipType page', () => {
    it('should have translated page title', () => {
      cy.visit(partyRelationshipTypePageUrl);
      cy.getEntityHeading('PartyRelationshipType').should('not.contain', 'snpApp.partyRelationshipType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyRelationshipTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyRelationshipType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyRelationshipTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyRelationshipType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-relationship-types',
          body: partyRelationshipTypeSample,
        }).then(({ body }) => {
          partyRelationshipType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-relationship-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyRelationshipType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyRelationshipTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyRelationshipType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyRelationshipType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
      });

      it('edit button click should load edit PartyRelationshipType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRelationshipType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
      });

      it('edit button click should load edit PartyRelationshipType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRelationshipType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
      });

      it('last delete button click should delete instance of PartyRelationshipType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyRelationshipType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipTypePageUrl);

        partyRelationshipType = undefined;
      });
    });
  });

  describe('new PartyRelationshipType page', () => {
    beforeEach(() => {
      cy.visit(partyRelationshipTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyRelationshipType');
    });

    it('should create an instance of PartyRelationshipType', () => {
      cy.get(`[data-cy="name"]`).type('quizzically next');
      cy.get(`[data-cy="name"]`).should('have.value', 'quizzically next');

      cy.get(`[data-cy="description"]`).type('informal');
      cy.get(`[data-cy="description"]`).should('have.value', 'informal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyRelationshipType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyRelationshipTypePageUrl);
    });
  });
});
