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

describe('PickList e2e test', () => {
  const pickListPageUrl = '/pick-list';
  let username: string;
  let password: string;
  const pickListSample = { pickListDate: '2023-12-14T12:14:35.503Z' };

  let pickList;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pick-lists+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pick-lists').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pick-lists/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pickList) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pick-lists/${pickList.id}`,
      }).then(() => {
        pickList = undefined;
      });
    }
  });

  it('PickLists menu should load PickLists page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pick-list');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PickList').should('exist');
    cy.location('pathname').should('eq', pickListPageUrl);
  });

  describe('PickList page', () => {
    it('should have translated page title', () => {
      cy.visit(pickListPageUrl);
      cy.getEntityHeading('PickList').should('not.contain', 'snpApp.pickList.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pickListPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PickList page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${pickListPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PickList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pick-lists',
          body: pickListSample,
        }).then(({ body }) => {
          pickList = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pick-lists+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pick-lists?page=0&size=20>; rel="last",<http://localhost/api/pick-lists?page=0&size=20>; rel="first"',
              },
              body: [pickList],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pickListPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PickList page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pickList');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListPageUrl);
      });

      it('edit button click should load edit PickList page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PickList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListPageUrl);
      });

      it('edit button click should load edit PickList page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PickList');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListPageUrl);
      });

      it('last delete button click should delete instance of PickList', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pickList').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pickListPageUrl);

        pickList = undefined;
      });
    });
  });

  describe('new PickList page', () => {
    beforeEach(() => {
      cy.visit(pickListPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PickList');
    });

    it('should create an instance of PickList', () => {
      cy.get(`[data-cy="description"]`).type('appreciate');
      cy.get(`[data-cy="description"]`).should('have.value', 'appreciate');

      cy.get(`[data-cy="pickListDate"]`).type('2023-12-14T08:26');
      cy.get(`[data-cy="pickListDate"]`).blur();
      cy.get(`[data-cy="pickListDate"]`).should('have.value', '2023-12-14T08:26');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pickList = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', pickListPageUrl);
    });
  });
});
