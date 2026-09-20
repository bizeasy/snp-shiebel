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

describe('PartyClassificationGroup e2e test', () => {
  const partyClassificationGroupPageUrl = '/party-classification-group';
  let username: string;
  let password: string;
  const partyClassificationGroupSample = { name: 'ill-fated as' };

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
    cy.intercept('GET', '/api/party-classification-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-classification-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-classification-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyClassificationGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-classification-groups/${partyClassificationGroup.id}`,
      }).then(() => {
        partyClassificationGroup = undefined;
      });
    }
  });

  it('PartyClassificationGroups menu should load PartyClassificationGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-classification-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyClassificationGroup').should('exist');
    cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
  });

  describe('PartyClassificationGroup page', () => {
    it('should have translated page title', () => {
      cy.visit(partyClassificationGroupPageUrl);
      cy.getEntityHeading('PartyClassificationGroup').should('not.contain', 'snpApp.partyClassificationGroup.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyClassificationGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyClassificationGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyClassificationGroupPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyClassificationGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-classification-groups',
          body: partyClassificationGroupSample,
        }).then(({ body }) => {
          partyClassificationGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-classification-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyClassificationGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyClassificationGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyClassificationGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyClassificationGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
      });

      it('edit button click should load edit PartyClassificationGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassificationGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
      });

      it('edit button click should load edit PartyClassificationGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyClassificationGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
      });

      it('last delete button click should delete instance of PartyClassificationGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyClassificationGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyClassificationGroupPageUrl);

        partyClassificationGroup = undefined;
      });
    });
  });

  describe('new PartyClassificationGroup page', () => {
    beforeEach(() => {
      cy.visit(partyClassificationGroupPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyClassificationGroup');
    });

    it('should create an instance of PartyClassificationGroup', () => {
      cy.get(`[data-cy="name"]`).type('notarize taxicab before');
      cy.get(`[data-cy="name"]`).should('have.value', 'notarize taxicab before');

      cy.get(`[data-cy="description"]`).type('mid hungry along');
      cy.get(`[data-cy="description"]`).should('have.value', 'mid hungry along');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyClassificationGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyClassificationGroupPageUrl);
    });
  });
});
